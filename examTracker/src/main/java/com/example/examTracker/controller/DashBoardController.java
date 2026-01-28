package com.example.examTracker.controller;

import com.example.examTracker.dto.dashboard.DashboardDaysResponseDTO;
import com.example.examTracker.dto.dashboard.DashboardResponseDTO;
import com.example.examTracker.dto.dashboard.DashboardTaskResponseDTO;
import com.example.examTracker.entity.AppUser;
import com.example.examTracker.entity.Task;
import com.example.examTracker.entity.UserExamStats;
import com.example.examTracker.exceptions.DashBoardAccessException;
import com.example.examTracker.repository.TaskRepository;
import com.example.examTracker.service.TaskService;
import com.example.examTracker.service.UserExamStatsService;
import com.example.examTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DashBoardController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserExamStatsService userExamStatsService;
    /***
     * Purpose:
     * When user hit this API, Backend will make sure User is subscribed to 1 exam. if not, backend will throw error and same as Banner FE will show to user.
     * It will give the FE response for previous 6 days and today.
     * Response contains, for previous days, which task user completed, not completed, isEditable=true/false
     * In every case, response will contains previous 7 days data, even is user is registering to app today.
     * For all the previous 6 days, isEditable will be false and FE should not worry about this, BE will give this data.
     * At front-end side:
     * if isEditable = false: for that task, Button should disabled.
     * if isEditable = true: Button is enabled.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard(Authentication authentication) {
        String email = authentication.getName();
        AppUser appUser = userService.getUserByEmail(email);

        // Get examId from userId
        UserExamStats userExamStats = userExamStatsService.getUserExamStats(appUser.getId());
        if(userExamStats == null) {
            throw new DashBoardAccessException("Exam details not found");
        }
        DashboardResponseDTO dashboardResponseDTO = new DashboardResponseDTO();

        ArrayList<Integer> resList = taskService.getUpdateStreak(appUser.getId());
        Integer currentStreak = resList.get(0);
        Integer longestStreak = resList.get(1);
        dashboardResponseDTO.setCurrentStreak(currentStreak);
        dashboardResponseDTO.setLongestStreak(longestStreak);
        // This list will contain previous six days and today's date. starting from t-6 date.
        ArrayList<LocalDate> sevenDays = new ArrayList<>();
        for (int i=6;i>=0;i--) {
            sevenDays.add(LocalDate.now().minusDays(i));
        }
        // iterating through all seven dates.
        ArrayList<DashboardDaysResponseDTO> dashboardDaysResponseDTOArrayList = new ArrayList<>();
        for (LocalDate date: sevenDays) {
            DashboardDaysResponseDTO dashboardDaysResponseDTO = new DashboardDaysResponseDTO();
            dashboardDaysResponseDTO.setDate(date);
            dashboardDaysResponseDTO.setDayComplete(taskService.isDayCompleted(appUser.getId(), date));

            ArrayList<DashboardTaskResponseDTO> dashboardTaskResponseDTOArrayList = new ArrayList<>();

            // Get all tasks for the examId
            List<Task> allTasks = taskRepository.findAllTasksByExamId(userExamStats.getExam().getExamId());
            for (Task task: allTasks) {
                DashboardTaskResponseDTO dashboardTaskResponseDTO = new DashboardTaskResponseDTO();
                dashboardTaskResponseDTO.setTaskId(task.getTaskId());
                dashboardTaskResponseDTO.setTaskTitle(task.getTaskTitle());
                dashboardTaskResponseDTO.setTaskCompleted(taskService.isTaskCompleteForDate(appUser.getId(), task.getTaskId(), date));
                boolean isEditable = (date.equals(LocalDate.now()) && !taskService.isTaskCompleteForDate(appUser.getId(), task.getTaskId(), date)) ? true : false;
                dashboardTaskResponseDTO.setEditable(isEditable);
                dashboardTaskResponseDTOArrayList.add(dashboardTaskResponseDTO);
            }
            dashboardDaysResponseDTO.setTasks(dashboardTaskResponseDTOArrayList);
            dashboardDaysResponseDTOArrayList.add(dashboardDaysResponseDTO);
        }
        dashboardResponseDTO.setDays(dashboardDaysResponseDTOArrayList);
        return ResponseEntity.ok(dashboardResponseDTO);
    }
}
