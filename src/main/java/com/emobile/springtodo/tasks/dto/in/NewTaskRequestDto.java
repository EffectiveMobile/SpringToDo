package com.emobile.springtodo.tasks.dto.in;

import com.emobile.springtodo.tasks.model.entity.Priority;
import com.emobile.springtodo.tasks.model.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record NewTaskRequestDto(
                                @NotBlank
                                String description,

                                @NotBlank
                                String header,

                                @NotBlank
                                Priority priority,

                                @NotBlank
                                Status status,

                                @Positive
                                Long assignee,

                                @Positive
                                Long author) {
}
