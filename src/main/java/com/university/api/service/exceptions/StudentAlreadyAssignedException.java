package com.university.api.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentAlreadyAssignedException extends RuntimeException {
    public StudentAlreadyAssignedException(String message) {
        super(message);
    }
}
