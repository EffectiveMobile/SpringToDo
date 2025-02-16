package com.emobile.springtodo.exception;

import com.emobile.springtodo.dto.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTodoNotFoundException(TodoNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        Instant.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "Invalid request body or parameters. Please check your input.";

        if (ex.getCause() instanceof InvalidFormatException invalidFormat) {
            message = "Invalid value for field: " + invalidFormat.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst()
                    .orElse("unknown") +
                    ". Allowed values: " + Arrays.toString(invalidFormat.getTargetType().getEnumConstants());
        }

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        message,
                        Instant.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        Instant.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred: " + ex.toString() + " " + ex.getMessage() + " " + Arrays.toString(ex.getStackTrace()),
                        Instant.now()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
