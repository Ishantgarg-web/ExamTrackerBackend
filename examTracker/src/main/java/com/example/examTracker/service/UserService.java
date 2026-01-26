package com.example.examTracker.service;

import com.example.examTracker.dto.CreateTaskProgressResponseDTO;
import com.example.examTracker.entity.AppUser;
import com.example.examTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserExamStatsService userExamStatsService;

    @Autowired
    TaskService taskService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(appUser.getRole().name())
                .build();
    }

    public AppUser saveUser(AppUser appUser) {
        return userRepository.save(appUser);
    }
    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public CreateTaskProgressResponseDTO prepareResponseForCreateTaskProgress(String userId, String taskId) {
        // Get examId from userId
        String examId = userExamStatsService.getUserExamStats(userId).getExam().getExamId();
        return CreateTaskProgressResponseDTO.builder()
                .taskId(taskId)
                .completedStatus(true)
                .currentStreak(userExamStatsService.getCurrentStreakForUser(userId, examId))
                .longestStreak(userExamStatsService.getLongestStreakForUser(userId, examId))
                .dayCompleted(taskService.isDayCompleted(userId, LocalDate.now()))
                .build();
    }
}
