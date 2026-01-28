package com.example.examTracker.dto;

import com.example.examTracker.entity.Exam;
import com.example.examTracker.enums.BACHELOR_DEGREE;
import com.example.examTracker.enums.USERS_ROLE;
import com.example.examTracker.enums.WORKING_STATUS;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserProfileResponseDTO {

    private String userId;
    private String userName;
    private String userEmail;
    private String phoneNumber;
    private WORKING_STATUS workingStatus;
    private BACHELOR_DEGREE bachelorDegree;
    private List<UserExamResponseDTO> exams;

}
