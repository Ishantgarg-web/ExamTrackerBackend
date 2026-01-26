package com.example.examTracker.service;

import com.example.examTracker.entity.Task;
import com.example.examTracker.entity.UserTaskProgress;
import com.example.examTracker.repository.UserTaskProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserTaskProgressService {

    @Autowired
    UserTaskProgressRepository userTaskProgressRepository;

    public UserTaskProgress findByUserIdTaskIdCompletedAt(String userId, String taskId, LocalDate completedDate) {
        return userTaskProgressRepository.
                findByUserIdTaskIdCompletedAt(userId, taskId, completedDate);
    }

    public void save(UserTaskProgress userTaskProgress) {
        userTaskProgressRepository.save(userTaskProgress);
    }
}
