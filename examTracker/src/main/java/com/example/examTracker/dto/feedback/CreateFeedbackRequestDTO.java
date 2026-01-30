package com.example.examTracker.dto.feedback;

import com.example.examTracker.enums.FEEDBACK_REQUEST_TYPE;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateFeedbackRequestDTO {
    @JsonProperty("feedbackType")
    private FEEDBACK_REQUEST_TYPE feedbackRequestType;
    private String subject;
    private String message;
}
