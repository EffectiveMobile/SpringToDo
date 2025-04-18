package com.emobile.springtodo.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * DTO для представления ошибки в ответе API.
 * Используется для возврата информации об ошибках клиенту.
 *
 * @author Мельников Никита
 */
public record ErrorResponse(int code,
                            HttpStatus status,
                            String message,
                            Instant timestamp) {
}
