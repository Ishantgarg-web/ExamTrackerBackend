package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class TaskProgressAlreadyPresent extends ApiException {

    public TaskProgressAlreadyPresent(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
