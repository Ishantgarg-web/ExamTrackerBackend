package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadySubscribeOneExam extends ApiException {

    public UserAlreadySubscribeOneExam(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
