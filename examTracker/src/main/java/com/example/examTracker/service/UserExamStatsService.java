package com.example.examTracker.service;

import com.example.examTracker.entity.Exam;
import com.example.examTracker.entity.UserExamStats;
import com.example.examTracker.repository.UserExamStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserExamStatsService {

    @Autowired
    UserExamStatsRepository userExamStatsRepository;

    // Get UserExamStats for a user
    public UserExamStats getUserExamStats(String userId) {
        return userExamStatsRepository.findByUserId(userId);
    }

    public UserExamStats existsByUserAndExam(String userId, String examId) {
        return userExamStatsRepository
                .existsByUserAndExam(userId, examId);
    }

    public void save(UserExamStats userExamStats) {
        userExamStatsRepository.save(userExamStats);
    }

    public UserExamStats existsByUser(String id) {
        return userExamStatsRepository.findByUserId(id);
    }
}
