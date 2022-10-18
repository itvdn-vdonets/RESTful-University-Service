package com.university.api.service.aop;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    private ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String answer = "The error description";
        return handleExceptionInternal(ex, answer, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

}
