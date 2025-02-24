package com.emobile.springtodo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Ответ с ошибкой")
public class ErrorResponse {

    @Schema(description = "Время возникновения ошибки", example = "2023-10-24T15:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP статус код ошибки", example = "400")
    private int status;

    @Schema(description = "Тип ошибки", example = "Bad Request")
    private String error;

    @Schema(description = "Сообщение об ошибке", example = "Задача не найдена")
    private String message;

    @Schema(description = "Путь запроса, который вызвал ошибку", example = "/api/todos/1")
    private String path;

    @Schema(description = "Подробности ошибок валидации, если есть", example = "{\"field\":\"Title cannot be blank\"}")
    private Map<String, String> details;
}
