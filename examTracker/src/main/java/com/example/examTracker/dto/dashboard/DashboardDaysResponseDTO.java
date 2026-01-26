package com.example.examTracker.dto.dashboard;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDaysResponseDTO {
    private LocalDate date;
    private boolean isDayComplete;
    private List<DashboardTaskResponseDTO> tasks;
}
