package com.example.examTracker.service;

import com.example.examTracker.entity.Exam;
import com.example.examTracker.enums.EXAM_CODE;
import com.example.examTracker.repository.ExamRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    ExamRepository examRepository;

    public Exam getExamByExamId(String examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    public Exam getExamByExamCode(EXAM_CODE examCode) {
        return examRepository.findByexamCode(examCode)
                .orElseThrow(() -> new RuntimeException("Exam Code Not found"));
    }

    public void saveExam(Exam exam) {
        examRepository.save(exam);
    }

    public Exam findByExamCodeAndIsExamActiveTrue(EXAM_CODE examCode) {
        return examRepository
                .findByExamCodeAndIsExamActiveTrue(examCode.toString());
    }
}
