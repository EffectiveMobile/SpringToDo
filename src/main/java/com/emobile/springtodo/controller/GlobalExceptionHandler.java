package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.ErrorResponse;
import com.emobile.springtodo.exception.InvalidToDoException;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildResponse(Exception ex, HttpStatus status, HttpServletRequest request, Map<String, String> validationErrors) {
        log.error("Exception: {} - {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);


        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();


        if (validationErrors != null && !validationErrors.isEmpty()) {
            response.setDetails(validationErrors);
        }

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ToDoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ToDoNotFoundException ex, HttpServletRequest request) {
        return buildResponse(ex, HttpStatus.NOT_FOUND, request, null);
    }

    @ExceptionHandler(InvalidToDoException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToDo(InvalidToDoException ex, HttpServletRequest request) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }


        log.error("Validation failed: {}", validationErrors);


        return buildResponse(ex, HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        return buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, null);
    }
}
