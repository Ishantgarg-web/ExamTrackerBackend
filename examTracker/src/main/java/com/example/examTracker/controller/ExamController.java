package com.example.examTracker.controller;

import com.example.examTracker.dto.CreateExamRequestDTO;
import com.example.examTracker.entity.Exam;
import com.example.examTracker.exceptions.ExamAlreadyExistsException;
import com.example.examTracker.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        // Check if exam already exists
        if (examService.existsByExamCode(createExamRequestDTO.getExamCode())) {
            throw new ExamAlreadyExistsException("Exam with code " + createExamRequestDTO.getExamCode() + " already exists");
        }
        
        // Exam doesn't exist, create it
        Exam exam = Exam.builder()
                .examCode(createExamRequestDTO.getExamCode())
                .examName(createExamRequestDTO.getExamName())
                .build();
        examService.saveExam(exam);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Exam created successfully with code: " + createExamRequestDTO.getExamCode());
    }

}
