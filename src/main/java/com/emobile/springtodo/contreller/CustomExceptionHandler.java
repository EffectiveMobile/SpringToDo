package com.emobile.springtodo.contreller;

import com.emobile.springtodo.exceptions.ErrorInputDataException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
