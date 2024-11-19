package com.emobile.springtodo.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Schema(description = "Модель задачи, связанной с определенной категорией.")
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Task extends BaseEntity {

    @Schema(description = "Уникальный идентификатор задачи", example = "d3f5c7b1-2e4f-6a8b-9c0d-e1f3g4a5b6c7")
    private UUID taskID;

    @Schema(description = "Название задачи", example = "Сделать валидацию DTO")
    private String taskTitle;

    @Schema(description = "Описание задачи", example = "Необходимо валидировать реквесты за утро")
    private String taskDescription;

    @Schema(description = "Статус задачи", example = "TODO")
    private Status taskStatus;

    @Schema(description = "Дата выполнения задачи в формате yyyy-MM-dd", example = "2024-11-13")
    private Date taskDate;

    @Schema(description = "Количество часов, отведенных на выполнение задачи", example = "3.5")
    private Float taskHours;

    @Schema(description = "Уникальный идентификатор категории, к которой относится задача", example = "2cb7304c-0116-464c-94e5-e4ea8b799460")
    private UUID taskCategory;
}
