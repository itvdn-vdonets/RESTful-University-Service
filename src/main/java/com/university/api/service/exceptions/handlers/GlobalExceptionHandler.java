package com.university.api.service.exceptions.handlers;

import com.university.api.service.models.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable cause = ex.getCause() == null ? ex : ex.getCause();
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiErrorResponse errorResponse = createErrorResponse(cause, msg);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(),
                HttpStatus.valueOf(errorResponse.getStatus()), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> handleCustomRuntimeException(Exception ex, WebRequest request) {
        Throwable cause = ex.getCause() == null ? ex : ex.getCause();
        String msg = cause.getMessage();
        ApiErrorResponse errorResponse = createErrorResponse(cause, msg);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(),
                HttpStatus.valueOf(errorResponse.getStatus()), request);
    }

    private <T> ApiErrorResponse createErrorResponse(T objects, String message) {
        ResponseStatus responseStatus = objects.getClass().getAnnotation(ResponseStatus.class);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (Objects.nonNull(responseStatus)) {
            httpStatus = responseStatus.value();
        }

        return new ApiErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }
}