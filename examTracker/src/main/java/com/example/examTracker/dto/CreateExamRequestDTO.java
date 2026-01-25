package com.example.examTracker.dto;

import com.example.examTracker.enums.EXAM_CODE;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExamRequestDTO {
    private EXAM_CODE examCode;
    private String examName;
}
