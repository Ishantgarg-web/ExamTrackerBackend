package com.example.examTracker.service;


import com.example.examTracker.entity.Task;
import com.example.examTracker.entity.UserTaskProgress;
import com.example.examTracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserTaskProgressService userTaskProgressService;

    @Autowired
    UserExamStatsService userExamStatsService;

    @Autowired
    ExamService examService;

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public Task findByTaskTitleAndExamId(String taskTitle, String examId) {
        return taskRepository.findByTaskTitleAndExamId(taskTitle, examId);
    }

    public Task findById(String taskId) {
        return taskRepository.findById(taskId).get();
    }

    /**
     * update User streak if applicable for that exam.
     * Streak Logic:
     * previousDayTasksNotCompleted:
     * 	TodayAllTasksCompleted: updated CS = 1, LS = max(stored_LS, 1)
     * 	TodayAllTasksNotCompleted: updated CS = 0, LS = stored_LS
     * previousDayTasksCompleted:
     * 	TodayAllTasksCompleted:	updated CS = Stored_CS + 1, LS = max(stored_LS, updated_CS)
     * 	TodayAllTasksNotCompleted: updated CS = Stored_CS, LS = stored_LS
     */
    public void updateStreakLogic(String userId) {
        // Get examId from userId
        String examId = userExamStatsService.getUserExamStats(userId).getExam().getExamId();
        // Get all tasks for the examId
        List<Task> allTasks = taskRepository.findAllTasksByExamId(examId);

        if(isCompletedAllTasks(userId, allTasks, LocalDate.now().minusDays(1))) {
            if(isCompletedAllTasks(userId, allTasks, LocalDate.now())) {
                // updated CS = Stored_CS + 1, LS = max(stored_LS, updated_CS)
                Integer currentStreak = userExamStatsService.getCurrentStreakForUser(userId, examId);
                userExamStatsService.updateCurrentStreak(userId, examId, currentStreak + 1);
                Integer longestStreak = userExamStatsService.getLongestStreakForUser(userId, examId);
                userExamStatsService.updateLongestStreak(userId, examId,
                        (int)Math.max(longestStreak, currentStreak + 1));
            } else {
                // No need to update anything
            }
        } else {
            if(isCompletedAllTasks(userId, allTasks, LocalDate.now())) {
                // updated CS = 1, LS = max(stored_LS, 1)
                userExamStatsService.updateCurrentStreak(userId, examId, 1);
                userExamStatsService.updateLongestStreak(userId, examId,
                        (int)Math.max(userExamStatsService.getLongestStreakForUser(userId, examId), 1));
            } else {
                // updated CS = 0, LS = stored_LS
                userExamStatsService.updateCurrentStreak(userId, examId, 0);
                // longest streak no need to update.
            }
        }
    }

    /**
     * Purpose: To get updated current streak and longest streak for the user.
     * This method will be used by getProfile API.
     *
     * Logic:
     * previousDayTasksNotCompleted:
     * 	TodayAllTasksCompleted: updated CS = 1, LS = max(stored_LS, 1)
     * 	TodayAllTasksNotCompleted: updated CS = 0, LS = stored_LS
     * previousDayTasksCompleted:
     * 	CS = getCS
     * 	LS = getLS
     * @param userId
     */
    public ArrayList<Integer> getUpdateStreak(String userId) {
        // Get examId from userId
        String examId = userExamStatsService.getUserExamStats(userId).getExam().getExamId();
        // Get all tasks for the examId
        List<Task> allTasks = taskRepository.findAllTasksByExamId(examId);
        ArrayList<Integer> resList = new ArrayList<>();
        if(isCompletedAllTasks(userId, allTasks, LocalDate.now().minusDays(1))) {
            resList.add(userExamStatsService.getCurrentStreakForUser(userId, examId));
            resList.add(userExamStatsService.getLongestStreakForUser(userId, examId));
        } else {
            if(isCompletedAllTasks(userId, allTasks, LocalDate.now())) {
                userExamStatsService.updateCurrentStreak(userId, examId,  1);
                userExamStatsService.updateLongestStreak(userId, examId,
                        (int)Math.max(userExamStatsService.getLongestStreakForUser(userId, examId), 1));
            } else {
                userExamStatsService.updateCurrentStreak(userId, examId,  0);
                // Longest streak no need to update
            }
            resList.add(userExamStatsService.getCurrentStreakForUser(userId, examId));
            resList.add(userExamStatsService.getLongestStreakForUser(userId, examId));
        }
        return resList;
    }

    private boolean isCompletedAllTasks(String userId, List<Task> allTasks, LocalDate date) {
        for (Task task: allTasks) {
            // if that task is not present in user_task_progress table, there will be no Streak Logic update.
            // if that task is present:
            // check userId, taskId, and completed_at = today's date, then continue
            // else no need to update Streak Logic
            UserTaskProgress userTaskProgress = userTaskProgressService.findByUserIdTaskIdCompletedAt(userId, task.getTaskId(), date);
            if(userTaskProgress == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isDayCompleted(String userId, LocalDate date) {
        // Get examId from userId
        String examId = userExamStatsService.getUserExamStats(userId).getExam().getExamId();
        // Get all tasks for the examId
        List<Task> allTasks = taskRepository.findAllTasksByExamId(examId);
        return isCompletedAllTasks(userId, allTasks, date);
    }

    public boolean isTaskCompleteForDate(String userId, String taskId, LocalDate date) {
        UserTaskProgress userTaskProgress = userTaskProgressService.findByUserIdTaskIdCompletedAt(userId, taskId, date);
        if(userTaskProgress == null) {
            return false;
        }
        return true;
    }
}
