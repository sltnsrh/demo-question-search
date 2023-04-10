package com.salatin.similarsearch.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler(value = {
        ConstraintViolationException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiExceptionObject> handleBadRequestException(
        RuntimeException e
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(getApiExceptionObject(e.getMessage(), status), status);
    }

    private ApiExceptionObject getApiExceptionObject(String message, HttpStatus status) {
        return new ApiExceptionObject(
            message,
            status,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN))
        );
    }
}
