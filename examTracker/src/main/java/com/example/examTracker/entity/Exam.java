package com.example.examTracker.entity;

import com.example.examTracker.enums.EXAM_CODE;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.*;

import java.time.Instant;

/**
 * Master entity for competitive exams (CAT, JEE, etc).
 * Defines which exams are supported and whether they are active on the platform.
 */

@Builder
@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String examId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EXAM_CODE examCode; // CAT, JEE, GATE

    @Column(nullable = false)
    private String examName;

    @Builder.Default
    @Column(nullable = false)
    private boolean isExamActive = true;

    @CreationTimestamp
    private Instant createdAt;
}

