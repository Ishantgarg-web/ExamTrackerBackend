package com.example.examTracker.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Tracks a user's completion of a specific task on a specific day.
 * Used for streak calculation, daily progress, and historical analysis.
 */


@Entity
@Table(
        name = "user_task_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "task_id", "progress_date"}
                )
        }
)
public class UserTaskProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    // Stored in UTC
    @Column(nullable = false)
    private Instant completedAt;

    // Derived from completedAt + user timezone
    @Column(nullable = false)
    private LocalDate progressDate;

    private boolean completed;
}

/**
 * Example for completedAt and progress_date
 * User timezone: America/New_York
 * completedAt: 2026-01-11T01:00Z
 * progress_date: 2026-01-10
 */