package com.example.examTracker.dto.feedback;

import com.example.examTracker.enums.FEEDBACK_REQUEST_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedbackResponseDTO {
    private FEEDBACK_REQUEST_TYPE feedbackRequestType;
    private String subject;
    private String message;
}
