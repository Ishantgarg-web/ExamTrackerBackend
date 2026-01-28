package com.example.examTracker.exceptions;

import org.springframework.http.HttpStatus;

public class OAuthException extends ApiException {

    public OAuthException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public OAuthException(String message, HttpStatus status) {
        super(message, status);
    }
}
