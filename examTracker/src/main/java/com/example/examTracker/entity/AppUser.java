package com.example.examTracker.entity;

import com.example.examTracker.enums.BACHELOR_DEGREE;
import com.example.examTracker.enums.USERS_ROLE;
import com.example.examTracker.enums.WORKING_STATUS;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Core user entity.
 * Represents a single platform user authenticated via Google/JWT.
 * Stores global user information (profile, role, timezone) independent of exams.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private USERS_ROLE role = USERS_ROLE.USER;

    // 🔥 IMPORTANT: User timezone
    @Column(nullable = false)
    private String timeZone; // e.g. "Asia/Kolkata", "America/New_York"

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt; // UTC

    @UpdateTimestamp
    private Instant updatedAt; // UTC

    @Column
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private WORKING_STATUS workingStatus; // STUDENT, WORKING

    @Enumerated(EnumType.STRING)
    private BACHELOR_DEGREE bachelorDegree;
}
