package com.example.examTracker.controller;

import com.example.examTracker.dto.feedback.CreateFeedbackRequestDTO;
import com.example.examTracker.dto.feedback.CreateFeedbackResponseDTO;
import com.example.examTracker.service.FeedbackService;
import com.example.examTracker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FeedbackController {

    @Autowired
    UserService userService;

    @Autowired
    FeedbackService feedbackService;

    private static Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @PostMapping("/feedback")
    public ResponseEntity<CreateFeedbackResponseDTO> createFeedback(
            Authentication authentication,
            @RequestBody CreateFeedbackRequestDTO createFeedbackRequestDTO) {
        String email = authentication.getName();
        try {
            CreateFeedbackResponseDTO createFeedbackResponseDTO = feedbackService.saveFeedback(email, createFeedbackRequestDTO);
            return ResponseEntity.ok(createFeedbackResponseDTO);
        } catch (Exception e) {
            logger.info("feedback api failing errorMessage: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
