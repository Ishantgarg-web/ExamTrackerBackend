package com.example.examTracker.dto.dashboard;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTaskResponseDTO {
    private String taskId;
    private String taskTitle;
    private boolean isTaskCompleted;
    private boolean isEditable;
}
