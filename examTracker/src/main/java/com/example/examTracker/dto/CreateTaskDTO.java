package com.example.examTracker.dto;

import com.example.examTracker.entity.Exam;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {
    private String examId;
    private String taskTitle;
    private Integer dailyTarget;
}
