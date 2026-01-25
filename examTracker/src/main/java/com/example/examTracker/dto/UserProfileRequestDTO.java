package com.example.examTracker.dto;

import com.example.examTracker.enums.BACHELOR_DEGREE;
import com.example.examTracker.enums.WORKING_STATUS;
import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDTO {
    private String phoneNumber;
    private WORKING_STATUS workingStatus;
    private BACHELOR_DEGREE bachelorDegree;
    private String timeZone;
}
