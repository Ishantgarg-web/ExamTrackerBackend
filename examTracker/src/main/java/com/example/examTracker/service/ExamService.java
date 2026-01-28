package com.example.examTracker.service;

import com.example.examTracker.entity.Exam;
import com.example.examTracker.enums.EXAM_CODE;
import com.example.examTracker.exceptions.ExamNotFoundException;
import com.example.examTracker.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {

    @Autowired
    ExamRepository examRepository;

    public Exam getExamByExamId(String examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with id: " + examId));
    }

    public Exam getExamByExamCode(EXAM_CODE examCode) {
        return examRepository.findByexamCode(examCode)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found with code: " + examCode));
    }

    public void saveExam(Exam exam) {
        examRepository.save(exam);
    }

    public Exam findByExamCodeAndIsExamActiveTrue(EXAM_CODE examCode) {
        return examRepository
                .findByExamCodeAndIsExamActiveTrue(examCode.toString());
    }

    public boolean existsByExamCode(EXAM_CODE examCode) {
        return examRepository.findByexamCode(examCode).isPresent();
    }
}
