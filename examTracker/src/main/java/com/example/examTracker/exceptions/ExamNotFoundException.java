package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class ExamNotFoundException extends ApiException {

    public ExamNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
