package com.example.examTracker.controller;


import com.example.examTracker.dto.*;
import com.example.examTracker.entity.*;
import com.example.examTracker.enums.BACHELOR_DEGREE;
import com.example.examTracker.enums.WORKING_STATUS;
import com.example.examTracker.exceptions.*;
import com.example.examTracker.exceptions.UserProfileExceptions.BachelorDegreeException;
import com.example.examTracker.exceptions.UserProfileExceptions.WorkingStatusException;
import com.example.examTracker.repository.ExamRepository;
import com.example.examTracker.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.examTracker.validations.PhoneNumberValidator.validatePhoneNumber;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserExamStatsService userExamStatsService;

    @Autowired
    ExamService examService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserTaskProgressService userTaskProgressService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     *Purpose
     *FE needs this to:
     *show profile info
     *know if onboarding is complete
     *display selected exams
     * @param authentication
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getUsersInfo(
            Authentication authentication
    ) {
        if(authentication == null) {
            logger.info("No auth object /me");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName(); // comes from JWT
        UserProfileResponseDTO userProfileResponseDTO =
                userService.prepareUserProfileResponseDTO(email, new UserProfileRequestDTO());
        return ResponseEntity.ok(userProfileResponseDTO);
    }

    /**
     * Purpose
     * Capture profile details after login
     * This completes “basic onboarding”
     * @param userProfileRequestDTO
     * @param authentication
     * @return
     */
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> updateUserProfile(@RequestBody UserProfileRequestDTO userProfileRequestDTO,
                                               Authentication authentication) {
        String email = null;
        try {
            email = authentication.getName();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Error handling for All these 4 params:
        if(!validatePhoneNumber(userProfileRequestDTO.getPhoneNumber())) {
            throw new PhoneNumberValidationException("Please provide correct phone number");
        }
        if(validatePhoneNumber(userProfileRequestDTO.getPhoneNumber())) {
            AppUser appUser = userService.getUserByEmail(email);
            appUser.setPhoneNumber(userProfileRequestDTO.getPhoneNumber() != null ? userProfileRequestDTO.getPhoneNumber() : appUser.getPhoneNumber());
            try {
                appUser.setWorkingStatus(userProfileRequestDTO.getWorkingStatus() != null ? userProfileRequestDTO.getWorkingStatus() : appUser.getWorkingStatus());
            } catch (Exception e) {
                throw new WorkingStatusException("Given working status not supported. please choose from them " + WORKING_STATUS.values());
            }
            try {
                appUser.setBachelorDegree(userProfileRequestDTO.getBachelorDegree() != null ? userProfileRequestDTO.getBachelorDegree() : appUser.getBachelorDegree());
            } catch (Exception e) {
                throw new BachelorDegreeException("Given Bachelor Degree not supported. please choose from them " + BACHELOR_DEGREE.values());
            }
            appUser.setTimeZone(userProfileRequestDTO.getTimeZone() != null ? userProfileRequestDTO.getTimeZone() : appUser.getTimeZone());
            userService.saveUser(appUser);
            UserProfileResponseDTO userProfileResponseDTO =
                    userService.prepareUserProfileResponseDTO(email, new UserProfileRequestDTO());
            return ResponseEntity.ok(userProfileResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Purpose
     * User chooses which exams he is preparing for
     * Creates entries in user_exam_stats
     * User can choose only 1 exam.
     * @param examRequestDto
     * @param authentication
     * @return
     */
    @PostMapping("/me/exams")
    public ResponseEntity<UserProfileResponseDTO> examPreparingFor(@RequestBody ExamRequestDto examRequestDto,
                                              Authentication authentication) {
        String email = authentication.getName();
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // validate exam code exist and active
        Exam exam = examService.findByExamCodeAndIsExamActiveTrue(examRequestDto.getExamCode());
        if(exam == null) {
            throw new WrongExamCodeException("Invalid exam code");
        }
        // For MVP, one user can subscribe to only one exam.
        // To check this, if userId already exist or not in the table, if exist throw exception, else create.
        // Get user from email
        AppUser appUser = userService.getUserByEmail(email);
        UserExamStats isUserExist = userExamStatsService
                .existsByUser(appUser.getId());
        if (isUserExist != null) {
            throw new UserAlreadySubscribeOneExam("User is already subscribe to one exam");
        }
        // Ensure uniqueness for user and exam
        UserExamStats userExamStatsObj = userExamStatsService
                .existsByUserAndExam(appUser.getId(), exam.getExamId());
        if (userExamStatsObj != null) {
            throw new ConflictException("Exam already selected: " + examRequestDto.getExamCode());
        }
        // create record in database
        UserExamStats userExamStats = UserExamStats.builder()
                .user(userService.getUserByEmail(email))
                .exam(exam)
                .attemptType(examRequestDto.getAttemptType())
                .currentStreak(0)
                .lastActiveAt(null)
                .longestStreak(0)
                .totalTasksCompleted(0)
                .build();
        userExamStatsService.save(userExamStats);
        UserProfileResponseDTO userProfileResponseDTO =
                userService.prepareUserProfileResponseDTO(email, new UserProfileRequestDTO());
        return ResponseEntity.ok(userProfileResponseDTO);
    }


    /**
     * Purpose
     * User record task progress
     * User can only mark Complete for a task in a day, once mark complete it will not change.
     * When user mark done for a task:
     *
     * Backend:
     * update user currentStreak → if all the task for that exam are done, and all are done today only.
     * update user LongestStreak → max(longestStreak, currentStreak)
     * update currentStreak to 0, if user for a day not able to complete all tasks.
     * @param createTaskProgressDTO
     * @param authentication
     * @return
     */
    @PostMapping("/me/taskProgress")
    public ResponseEntity<CreateTaskProgressResponseDTO> createTaskProgress(@RequestBody CreateTaskProgressDTO createTaskProgressDTO,
                                                Authentication authentication) {
        String email = authentication.getName();
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AppUser appUser = userService.getUserByEmail(email);
        // validate {userId, taskId, completed_at} should be unique
        UserTaskProgress userTaskProgressExist = userTaskProgressService.findByUserIdTaskIdCompletedAt(appUser.getId(),
                createTaskProgressDTO.getTaskId(),
                LocalDate.now());
        if(userTaskProgressExist != null) {
            throw new TaskProgressAlreadyPresent("Task is already present");
        }
        // create task progress record
        UserTaskProgress userTaskProgress = UserTaskProgress.builder()
                .user(appUser)
                .task(taskService.findById(createTaskProgressDTO.getTaskId()))
                .completed(true)
                .completedAt(LocalDate.now())
                .build();
        userTaskProgressService.save(userTaskProgress);
        taskService.updateStreakLogic(appUser.getId());
        CreateTaskProgressResponseDTO createTaskProgressResponseDTO =
                userService.prepareResponseForCreateTaskProgress(appUser.getId(), createTaskProgressDTO.getTaskId());
        return ResponseEntity.ok(createTaskProgressResponseDTO);
    }
}
