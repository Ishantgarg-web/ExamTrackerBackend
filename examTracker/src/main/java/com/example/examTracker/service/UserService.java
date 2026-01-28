package com.example.examTracker.service;

import com.example.examTracker.dto.CreateTaskProgressResponseDTO;
import com.example.examTracker.dto.UserExamResponseDTO;
import com.example.examTracker.dto.UserProfileRequestDTO;
import com.example.examTracker.dto.UserProfileResponseDTO;
import com.example.examTracker.entity.AppUser;
import com.example.examTracker.entity.Exam;
import com.example.examTracker.entity.UserExamStats;
import com.example.examTracker.exceptions.PhoneNumberValidationException;
import com.example.examTracker.exceptions.UserNotFoundException;
import com.example.examTracker.repository.UserRepository;
import com.example.examTracker.validations.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserExamStatsService userExamStatsService;

    @Autowired
    TaskService taskService;

    @Autowired
    ExamService examService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(appUser -> User.builder()
                        .username(appUser.getEmail())
                        .password(appUser.getPassword())
                        .roles(appUser.getRole().name())
                        .build()
                )
                .orElse(null);
    }


    public AppUser saveUser(AppUser appUser) {
        return userRepository.save(appUser);
    }
    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
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

    public UserProfileResponseDTO prepareUserProfileResponseDTO(String email,
                                                                UserProfileRequestDTO userProfileRequestDTO) {
        AppUser appUser = this.getUserByEmail(email);
        // prepare UserExamResponseDTO
        List<UserExamResponseDTO> userExamResponseDTOList = new ArrayList<>();
        UserExamStats userExamStats = userExamStatsService.getUserExamStats(appUser.getId());
        Exam exam = null;
        if(userExamStats != null) {
            exam = examService.getExamByExamId(userExamStats.getExam().getExamId());
        }
        ArrayList<Integer> currentStreakList = taskService.getUpdateStreak(appUser.getId());
// first: currentStreak, second: updatedStreak
// Safely extract values with defaults
        Integer currentStreak = (currentStreakList != null && currentStreakList.size() > 0)
                ? currentStreakList.get(0) : 0;
        Integer longestStreak = (currentStreakList != null && currentStreakList.size() > 1)
                ? currentStreakList.get(1) : 0;

// Handle null values in the list (in case getCurrentStreakForUser/getLongestStreakForUser return null)
        if (currentStreak == null) currentStreak = 0;
        if (longestStreak == null) longestStreak = 0;

        UserExamResponseDTO userExamResponseDTO = UserExamResponseDTO.builder()
                .examId(exam == null ? null : exam.getExamId())
                .examCode(exam == null ? null : exam.getExamCode())
                .attemptType(userExamStats != null ? userExamStats.getAttemptType() : null)
                .currentStreak(currentStreak)
                .longestStreak(longestStreak)
                .build();
        userExamResponseDTOList.add(userExamResponseDTO);

        UserProfileResponseDTO response = UserProfileResponseDTO.builder()
                .userId(appUser.getId())
                .userName(appUser.getUsername())
                .userEmail(appUser.getEmail())
                .phoneNumber(appUser.getPhoneNumber())
                .workingStatus(appUser.getWorkingStatus())
                .bachelorDegree(appUser.getBachelorDegree())
                .exams(userExamResponseDTOList)
                .build();
        return response;
    }
}
