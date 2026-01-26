package com.example.examTracker.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskProgressResponseDTO {
    private String taskId;
    private boolean completedStatus;
    private Integer currentStreak;
    private Integer longestStreak;
    private boolean dayCompleted;
}
