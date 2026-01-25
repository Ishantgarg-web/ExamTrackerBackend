package com.example.examTracker.repository;

import com.example.examTracker.entity.Exam;
import com.example.examTracker.entity.UserExamStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExamStatsRepository extends JpaRepository<UserExamStats, String> {

    UserExamStats findByUserId(String userId);

    @Query(
            value = "SELECT * FROM user_exam_stats ues WHERE ues.user_id = :userId AND ues.exam_exam_id = :examId",
            nativeQuery = true
    )
    UserExamStats existsByUserAndExam(@Param("userId") String userId, @Param("examId") String examId);
}
