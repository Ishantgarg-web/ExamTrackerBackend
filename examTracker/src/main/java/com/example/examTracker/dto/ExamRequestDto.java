package com.example.examTracker.dto;

import com.example.examTracker.enums.ATTEMPT_TYPE;
import com.example.examTracker.enums.EXAM_CODE;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequestDto {
    private EXAM_CODE examCode;
    private ATTEMPT_TYPE attemptType;
}
