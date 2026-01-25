package com.example.examTracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Represents a daily task associated with a specific exam.
 * Example: "Solve 20 QA questions" for CAT.
 * Tasks are reusable across users and tracked per user via UserTaskProgress.
 */

@Builder
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(nullable = false)
    private String taskTitle; // "Solve 20 QA questions"

    @Column(nullable = false)
    private int dailyTarget; // 20

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}

