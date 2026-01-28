package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class ExamAlreadyExistsException extends ApiException {

    public ExamAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
