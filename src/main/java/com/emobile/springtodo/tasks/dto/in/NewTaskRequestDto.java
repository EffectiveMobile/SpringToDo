package com.emobile.springtodo.tasks.dto.in;

import com.emobile.springtodo.tasks.model.entity.Priority;
import com.emobile.springtodo.tasks.model.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record NewTaskRequestDto(@Schema(description = "Text commentary/Текст комментария")
                                @NotBlank
                                String description,

                                @Schema(description = "Header task/Заголовок задачи")
                                @NotBlank
                                String header,

                                @Schema(description = "Task status LOW, MEDIUM, HIGH/Статус задачи LOW, MEDIUM, HIGH")
                                @NotBlank
                                Priority priority,

                                @Schema(description = "Task status WAITING, IN_PROCESS, COMPLETED/Статус задачи WAITING, IN_PROCESS, COMPLETED")
                                @NotBlank
                                Status status,

                                @Schema(description = "assignee/исполнитель")
                                @Positive
                                Long assignee,

                                @Schema(description = "author/автора")
                                @Positive
                                Long author) {
}
