package com.example.examTracker.service;

import com.example.examTracker.dto.feedback.CreateFeedbackRequestDTO;
import com.example.examTracker.dto.feedback.CreateFeedbackResponseDTO;
import com.example.examTracker.entity.AppUser;
import com.example.examTracker.entity.Feedback;
import com.example.examTracker.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    UserService userService;

    public CreateFeedbackResponseDTO saveFeedback(String email, CreateFeedbackRequestDTO createFeedbackRequestDTO) {
        Feedback feedback = Feedback.builder()
                .feedbackRequestType(createFeedbackRequestDTO.getFeedbackRequestType())
                .subject(createFeedbackRequestDTO.getSubject())
                .message(createFeedbackRequestDTO.getMessage())
                .user(userService.getUserByEmail(email))
                .build();
        feedbackRepository.save(feedback);
        return CreateFeedbackResponseDTO.builder()
                .feedbackRequestType(createFeedbackRequestDTO.getFeedbackRequestType())
                .subject(createFeedbackRequestDTO.getSubject())
                .message(createFeedbackRequestDTO.getMessage())
                .build();
    }

}
