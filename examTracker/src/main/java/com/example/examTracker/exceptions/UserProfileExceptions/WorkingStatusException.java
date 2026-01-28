package com.example.examTracker.exceptions.UserProfileExceptions;

import com.example.examTracker.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class WorkingStatusException extends ApiException {

    public WorkingStatusException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
