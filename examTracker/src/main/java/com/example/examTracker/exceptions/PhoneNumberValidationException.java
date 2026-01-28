package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class PhoneNumberValidationException extends ApiException {

    public PhoneNumberValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
