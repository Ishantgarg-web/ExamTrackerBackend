package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class WrongExamCodeException extends ApiException {

    public WrongExamCodeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}