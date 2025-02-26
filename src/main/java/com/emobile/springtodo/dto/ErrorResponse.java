package com.emobile.springtodo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Ответ с ошибкой, возвращаемый при возникновении исключения в приложении.
 * Содержит информацию о времени возникновения ошибки, статусе HTTP, типе ошибки, сообщении, пути запроса и деталях ошибки.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Ответ с ошибкой")
public class ErrorResponse {

    /**
     * Время возникновения ошибки.
     */
    @Schema(description = "Время возникновения ошибки", example = "2023-10-24T15:30:00")
    private LocalDateTime timestamp;

    /**
     * HTTP статус код ошибки.
     */
    @Schema(description = "HTTP статус код ошибки", example = "400")
    private int status;

    /**
     * Тип ошибки (например, Bad Request).
     */
    @Schema(description = "Тип ошибки", example = "Bad Request")
    private String error;

    /**
     * Сообщение об ошибке.
     */
    @Schema(description = "Сообщение об ошибке", example = "Задача не найдена")
    private String message;

    /**
     * Путь запроса, который вызвал ошибку.
     */
    @Schema(description = "Путь запроса, который вызвал ошибку", example = "/api/todos/1")
    private String path;

    /**
     * Подробности ошибок валидации, если они есть.
     */
    @Schema(description = "Подробности ошибок валидации, если есть", example = "{\"field\":\"Title cannot be blank\"}")
    private Map<String, String> details;
}
