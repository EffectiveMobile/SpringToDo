package com.emobile.springtodo.api.request;

import com.emobile.springtodo.api.request.validation.Patterns;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Date;

@Schema(description = "Схема запроса на создание задания")
public record TaskCreationRequest(

        @Schema(
                description = "Название задания",
                example = "Создание валидации",
                minLength = 2,
                maxLength = 64,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Title is required.")
        @Size(min = 2, max = 64, message = "Title is minimum 2 symbols and maximum 64 symbols.")
        String taskTitle,

        @Schema(
                description = "Описание задания",
                example = "Сделать валидирование за утро",
                minLength = 2,
                maxLength = 128,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Size(min = 2, max = 128, message = "Description is minimum 2 symbols and maximum 128 symbols.")
        String taskDescription,

        @Schema(
                description = "Статус задания",
                example = "TODO",
                pattern = Patterns.STATUS,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Status is required.")
        @Pattern(regexp = Patterns.STATUS,
                message = "Status must be one of: TODO, INPROGRESS, COMPLETE, UNCOMPLETED, DELAYED")
        String taskStatus,

        @Schema(
                description = "Дата выполнения задачи в формате yyyy-MM-dd",
                example = "2024-11-13",
                pattern = "yyyy-MM-dd",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Date is required.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date taskDate,

        @Schema(
                description = "Количество часов, отведенных на выполнение задачи",
                example = "3,5",
                minimum = "0,5",
                maximum = "24",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Positive(message = "Task hours must be positive")
        @Max(value = 24, message = "Task hours cannot exceed 24")
        @DecimalMin(value = "0.5", message = "Task hours must be at least 0.5")
        Float taskHours,

        @Schema(
                description = "Уникальный идентификатор категории, к которой относится задача",
                example = "2cb7304c-0116-464c-94e5-e4ea8b799460",
                pattern = Patterns.ID,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "UUID of category is required.")
        @Pattern(regexp = Patterns.ID,
                message = "Invalid UUID format for category.")
        String taskCategory) {
}
