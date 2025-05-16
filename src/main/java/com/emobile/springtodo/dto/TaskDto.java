package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.emobile.springtodo.validator.CheckEnum;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for task")
public class TaskDto {
    /**
     * Title of the task.
     */
    @NotBlank
    @Size(max = 100)
    @Schema(description = "Title of the task", example = "task1")
    private String title;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Description of the task", example = "Description")
    private String description;

    /**
     * Status of the task {PENDING, IN_PROGRESS, COMPLETED}.
     */
    @CheckEnum(enumClass = Status.class)
    @Schema(description = "Status of the task {PENDING, IN_PROGRESS, COMPLETED}", example = "PENDING")
    private String status;
}
