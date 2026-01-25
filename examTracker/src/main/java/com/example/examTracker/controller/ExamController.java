package com.example.examTracker.controller;

import com.example.examTracker.dto.CreateExamRequestDTO;
import com.example.examTracker.entity.Exam;
import com.example.examTracker.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/exam")
public class ExamController {

    @Autowired
    ExamService examService;

    @PostMapping("/create")
    public ResponseEntity<?> createExam(@RequestBody CreateExamRequestDTO createExamRequestDTO) {
        try {
            examService.getExamByExamCode(createExamRequestDTO.getExamCode());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            // create Exam, it is not present in database
            try {
                Exam exam = Exam.builder()
                        .examCode(createExamRequestDTO.getExamCode())
                        .examName(createExamRequestDTO.getExamName())
                        .build();
                examService.saveExam(exam);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }

}
