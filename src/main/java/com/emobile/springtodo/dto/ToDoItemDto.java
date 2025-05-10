package com.emobile.springtodo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO для задачи (ToDoItem).
 * Содержит поля задачи и использует аннотации для валидации данных.
 *
 * @author PavelOkhrimchuk
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoItemDto implements Serializable {

    /**
     * Уникальный идентификатор задачи.
     */
    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    private Long id;

    /**
     * Название задачи.
     * Не может быть пустым и должно быть не более 100 символов.
     */
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be at most 100 characters long")
    @Schema(description = "Название задачи", example = "Сделать уборку", maxLength = 100)
    private String title;

    /**
     * Описание задачи.
     * Должно быть не более 1000 символов.
     */
    @Size(max = 1000, message = "Description must be at most 1000 characters long")
    @Schema(description = "Описание задачи", example = "Убрать кухню и зал", maxLength = 1000)
    private String description;

    /**
     * Статус выполнения задачи.
     * true — задача выполнена, false — задача не выполнена.
     */
    @Schema(description = "Статус выполнения задачи", example = "false")
    private boolean completed;
}
