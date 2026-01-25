package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class TaskAlreadyExistException extends ApiException {

    public TaskAlreadyExistException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}