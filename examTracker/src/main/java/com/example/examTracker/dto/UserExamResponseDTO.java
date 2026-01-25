package com.example.examTracker.dto;

import com.example.examTracker.enums.ATTEMPT_TYPE;
import com.example.examTracker.enums.EXAM_CODE;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserExamResponseDTO {

    private String examId;
    private EXAM_CODE examCode;
    private ATTEMPT_TYPE attemptType;
    private int currentStreak;
    private int longestStreak;
}

