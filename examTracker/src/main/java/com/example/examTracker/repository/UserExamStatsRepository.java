package com.example.examTracker.repository;

import com.example.examTracker.entity.Exam;
import com.example.examTracker.entity.UserExamStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserExamStatsRepository extends JpaRepository<UserExamStats, String> {

    UserExamStats findByUserId(String userId);

    @Query(
            value = "SELECT * FROM user_exam_stats ues WHERE ues.user_id = :userId AND ues.exam_exam_id = :examId",
            nativeQuery = true
    )
    UserExamStats existsByUserAndExam(@Param("userId") String userId, @Param("examId") String examId);

    @Query(
            value = "SELECT current_streak FROM user_exam_stats ues WHERE ues.user_id = :userId AND ues.exam_exam_id = :examId",
            nativeQuery = true
    )
    int getCurrentStreakForUser(@Param("userId") String userId, @Param("examId") String examId);

    @Query(
            value = "update user_exam_stats ues set current_streak = :newCurrentStreak " +
                    "WHERE ues.user_id = :userId AND ues.exam_exam_id = :examId",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void updateCurrentStreak(@Param("userId") String userId,
                             @Param("examId") String examId,
                             @Param("newCurrentStreak") int newCurrentStreak);

    @Query(
            value = "update user_exam_stats ues set longest_streak = :newLongestStreak " +
                    "WHERE ues.user_id = :userId AND ues.exam_exam_id = :examId",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void updateLongestStreak(@Param("userId") String userId,
                             @Param("examId") String examId,
                             @Param("newLongestStreak") int newLongestStreak);

    @Query(
            value = "SELECT longest_streak FROM user_exam_stats ues WHERE ues.user_id = :userId AND ues.exam_exam_id = :examId",
            nativeQuery = true
    )
    Integer getLongestStreakForUser(@Param("userId") String userId, @Param("examId") String examId);
}
