package com.emobile.springtodo.core.exception;

import com.emobile.springtodo.core.exception.customexceptions.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildErrorResponse(RuntimeException e, HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("status", status.value());
        body.put("timestamp", Instant.now());
        body.put("error", e.getClass().getSimpleName());
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler({DataAccessException.class, CustomDataAccessException.class})
    public ResponseEntity<Map<String, Object>> handleDataAccessException(CustomDataAccessException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({CategoryNotFoundException.class, TaskNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({CategoryCreationException.class, TaskCreationException.class})
    public ResponseEntity<Map<String, Object>> handleCreationException(RuntimeException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({CategoryDeletionException.class, TaskDeletionException.class})
    public ResponseEntity<Map<String, Object>> handleDeletionException(RuntimeException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLockingFailureException(OptimisticLockingFailureException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(IllegalArgumentException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(constraintViolation -> {
                    String paramName = constraintViolation.getPropertyPath().toString();
                    paramName = paramName.substring(paramName.lastIndexOf('.') + 1);
                    return paramName + ": " + constraintViolation.getMessage();
                })
                .toList();

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {

            Map<String, String> errors = new HashMap<>();
            invalidFormatException.getPath().forEach(ref ->
                    errors.put(ref.getFieldName(), "Invalid date format. Expected format: yyyy-MM-dd")
            );

            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Malformed JSON request");
    }
}
