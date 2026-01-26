package com.example.examTracker.service;


import com.example.examTracker.entity.Task;
import com.example.examTracker.entity.UserTaskProgress;
import com.example.examTracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserTaskProgressService userTaskProgressService;

    @Autowired
    UserExamStatsService userExamStatsService;

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
     * First, Get all tasks for the subscribed exam
     *  find examId by taskId -> Get from tasks table
     *  Get all tasks for the given examId -> Get from tasks table
     * Second, check if all tasks are completed for today's date -> from UserTaskProgress table
     * third, if yes -> update currentStreak, and longestStreak(if applicable)
     * else, update currentStreak = 1 and longestStreak(if applicable)
     */
    public void updateStreakLogic(String taskId, String userId) {
        // find examId by taskId -> Get from tasks table
        String examId = taskRepository.findExamIdByTaskId(taskId);
        // Get all tasks for the given examId -> Get from tasks table
        List<Task> allTasks = taskRepository.findAllTasksByExamId(examId);
        // check if all tasks are completed for today's date
        boolean completedAllTasksForToday = isCompletedAllTasks(userId, allTasks, LocalDate.now());
        if(completedAllTasksForToday) {
            // then check if yesterday completed all tasks
            if(isCompletedAllTasks(userId, allTasks, LocalDate.now().minusDays(1))) {
                // update currentStreak = currentStreak + 1 in UserExamStats Table
                Integer currentStreak = userExamStatsService.getCurrentStreakForUser(userId, examId);
                userExamStatsService.updateCurrentStreak(userId, examId, currentStreak + 1);
                userExamStatsService.updateLongestStreak(userId, examId,
                        (int)Math.max(
                                userExamStatsService.getLongestStreakForUser(userId, examId),
                                currentStreak + 1
                        ));
            } else {
                // previous day tasks were not completed.
                userExamStatsService.updateCurrentStreak(userId, examId, 1);
                userExamStatsService.updateLongestStreak(userId, examId,
                        (int)Math.max(
                                userExamStatsService.getLongestStreakForUser(userId, examId),
                                1
                        ));
            }
        }
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
}
