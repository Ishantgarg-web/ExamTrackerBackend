package com.example.examTracker.entity;

import com.example.examTracker.enums.ATTEMPT_TYPE;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Junction entity between User and Exam.
 * Stores exam-specific data for a user such as attempt type, streaks,
 * and total task completion statistics.
 */


@Entity
@Table(
        name = "user_exam_stats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "exam_id"})
        }
)
public class UserExamStats {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Exam exam;

    private int totalTasksCompleted;

    private int currentStreak;

    private int longestStreak;

    @Column
    private Instant lastActiveAt; // UTC;

    @Enumerated(EnumType.STRING)
    private ATTEMPT_TYPE attemptType; // FIRST_TIME, REPEATER
}

