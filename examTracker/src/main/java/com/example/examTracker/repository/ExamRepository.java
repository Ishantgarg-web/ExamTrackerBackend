package com.example.examTracker.repository;

import com.example.examTracker.entity.Exam;
import com.example.examTracker.enums.EXAM_CODE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, String> {

    Optional<Exam> findByexamCode(EXAM_CODE examCode);

    @Query(
            value = "SELECT * FROM exams e WHERE e.exam_code = :examCode AND e.is_exam_active = true",
            nativeQuery = true
    )
    Exam findByExamCodeAndIsExamActiveTrue(@Param("examCode") String examCode);

}
