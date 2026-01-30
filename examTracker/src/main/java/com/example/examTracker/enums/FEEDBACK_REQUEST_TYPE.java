package com.example.examTracker.enums;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FEEDBACK_REQUEST_TYPE {
    BUG_REPORT,
    FEATURE_REQUEST,
    IMPROVEMENT_SUGGESTION,
    OTHER;

    @JsonCreator
    public static FEEDBACK_REQUEST_TYPE from(String value) {
        return switch (value.toLowerCase()) {
            case "bug" -> BUG_REPORT;
            case "feature" -> FEATURE_REQUEST;
            case "improvement" -> IMPROVEMENT_SUGGESTION;
            case "other" -> OTHER;
            default -> throw new IllegalArgumentException("Invalid feedback type: " + value);
        };
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

