package com.example.examTracker.entity;

import com.example.examTracker.enums.FEEDBACK_REQUEST_TYPE;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonProperty("feedbackType")
    @Enumerated(EnumType.STRING)
    private FEEDBACK_REQUEST_TYPE feedbackRequestType;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    private AppUser user;
}
