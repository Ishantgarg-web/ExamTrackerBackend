package com.example.examTracker.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskProgressDTO {
    private String taskId;
}
