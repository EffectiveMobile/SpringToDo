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

/**
 * Глобальный обработчик исключений.
 * Обрабатывает все исключения, возникающие в приложении,
 * и возвращает клиенту соответствующие HTTP-ответы.
 *
 * @author Мельников Никита
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение {@link TodoNotFoundException}.
     * Возвращает HTTP 404 (Not Found) с информацией об ошибке.
     *
     * @param ex исключение, связанное с отсутствием задачи
     * @return HTTP-ответ с кодом 404 и описанием ошибки
     */
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTodoNotFoundException(TodoNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(404,
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        Instant.now()),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение {@link HttpMessageNotReadableException}.
     * Возвращает HTTP 400 (Bad Request) с информацией об ошибке.
     *
     * @param ex исключение, связанное с ошибками парсинга JSON
     * @return HTTP-ответ с кодом 400 и описанием ошибки
     */
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
                new ErrorResponse(400,
                        HttpStatus.BAD_REQUEST,
                        message,
                        Instant.now()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение {@link InvalidDataException}.
     * Возвращает HTTP 400 (Bad Request) с информацией об ошибке.
     *
     * @param ex исключение, связанное с недопустимыми данными
     * @return HTTP-ответ с кодом 400 и описанием ошибки
     */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(400,
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        Instant.now()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает любые другие исключения.
     * Возвращает HTTP 500 (Internal Server Error) с информацией об ошибке.
     *
     * @param ex произвольное исключение
     * @return HTTP-ответ с кодом 500 и описанием ошибки
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse(500,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred: "
                                + ex.toString() + " "
                                + ex.getMessage() + " "
                                + Arrays.toString(ex.getStackTrace()),
                        Instant.now()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
