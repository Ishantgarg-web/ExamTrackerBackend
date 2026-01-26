package com.example.examTracker.dto.dashboard;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private Integer currentStreak;
    private Integer longestStreak;
    private List<DashboardDaysResponseDTO> days;
}



