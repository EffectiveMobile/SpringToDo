package com.emobile.springtodo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoItemDto implements Serializable {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be at most 100 characters long")
    @Schema(description = "Название задачи", example = "Сделать уборку", maxLength = 100)
    private String title;

    @Size(max = 1000, message = "Description must be at most 1000 characters long")
    @Schema(description = "Описание задачи", example = "Убрать кухню и зал", maxLength = 1000)
    private String description;

    @Schema(description = "Статус выполнения задачи", example = "false")
    private boolean completed;
}
