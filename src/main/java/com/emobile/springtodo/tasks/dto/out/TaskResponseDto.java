package com.emobile.springtodo.tasks.dto.out;

import com.emobile.springtodo.tasks.model.entity.Priority;
import com.emobile.springtodo.tasks.model.entity.Status;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record TaskResponseDto(@Schema(description = "id task/ид задачи")
                              Long id,

                              @Schema(description = "Task creation date and time/Время и дата создания задачи")
                              LocalDateTime creation,

                              @Schema(description = "Text commentary/Текст комментария")
                              String description,

                              @Schema(description = "Header task/Заголовок задачи")
                              String header,

                              @Schema(description = "Task status LOW, MEDIUM, HIGH/Статус задачи LOW, MEDIUM, HIGH")
                              Priority priority,

                              @Schema(description = "Task status WAITING, IN_PROCESS, COMPLETED/Статус задачи WAITING, IN_PROCESS, COMPLETED")
                              Status status,

                              @Schema(description = "author/автора")
                              UserResponseDto assignee,

                              @Schema(description = "assignee/исполнитель")
                              UserResponseDto author) {
}
