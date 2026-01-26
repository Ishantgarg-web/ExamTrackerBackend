package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class DashBoardAccessException extends ApiException {

    public DashBoardAccessException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
