package com.emobile.springtodo.controller;

import com.emobile.springtodo.exceptions.ErrorInputDataException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
    /**
     * Handles custom input data errors.
     */
    @ExceptionHandler(ErrorInputDataException.class)
    public ResponseStatusException errorInputDataException(ErrorInputDataException e) {
        log.error(e.getMessage());
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Handles SQL-related exceptions.
     */
    @ExceptionHandler(SQLException.class)
    public ResponseStatusException sqlException(SQLException e) {
        log.error(e.getMessage());
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * Handles validation errors in request arguments.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseStatusException methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder builder = new StringBuilder(e.getMessage());
        log.error(e.getMessage());
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, builder.substring(builder.lastIndexOf("[") + 1, builder.length() - 3));
    }

    /**
     * Handles constraint violations.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseStatusException constraintViolationException(ConstraintViolationException e) {
        StringBuilder builder = new StringBuilder(e.getMessage());
        log.error(e.getMessage());
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, builder.substring(builder.indexOf(".") + 1, builder.length() - 1));
    }

}
