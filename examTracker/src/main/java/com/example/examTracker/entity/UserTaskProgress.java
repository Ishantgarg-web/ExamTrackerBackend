package com.example.examTracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Tracks a user's completion of a specific task on a specific day.
 * Used for streak calculation, daily progress, and historical analysis.
 */


@Entity
@Table(
        name = "user_task_progress"
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDate completedAt;

    @Column(nullable = false)
    private boolean completed;
}

/**
 * Example for completedAt and progress_date
 * User timezone: America/New_York
 * completedAt: 2026-01-11T01:00Z
 * progress_date: 2026-01-10
 */