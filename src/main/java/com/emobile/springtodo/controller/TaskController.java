package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.Status;
import com.emobile.springtodo.service.impl.TaskServiceImpl;
import com.emobile.springtodo.validator.CheckEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for tasks.
 */
@RequestMapping("/api/task")
@RestController
@Validated
public class TaskController {
    private final TaskServiceImpl service;


    /**
     * Constructs a {@code TaskController} with the given task service.
     *
     * @param service the service for ToDoSpring
     */
    public TaskController(TaskServiceImpl service) {
        this.service = service;
    }


    /**
     * Show all task
     * Shows a paginated list of all existing tasks
     *
     * @param offset the starting index for pagination, default is 0
     * @param limit  the maximum number of tasks to return, default is 20
     * @return a list of {@link TaskDto} representing all tasks
     */
    @Operation(
            summary = "Show all task",
            description = "Shows a paginated list of all existing tasks",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    )
            }
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/showAll")
    public Page<TaskDto> showAllTasks(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                      @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit) {
        return service.getAllTasks(limit, offset);
    }

    /**
     * Retrieves a tasks by its title.
     *
     * @param title the title of the task
     * @return a list of {@link TaskDto} representing the task
     */
    @Operation(
            summary = "Find by title",
            description = "Allows you to find a task by title ",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/show/byTitle")
    public Page<TaskDto> showTaskByTitle(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                         @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit,
                                         @RequestParam @Parameter(description = "Title of the task you are looking for", required = true)
                                         @NotBlank @Size(max = 100) String title) {
        return service.getTaskByTitle(title, limit, offset);
    }

    /**
     * Deletes a task by title.
     *
     * @param title the title of the task to delete
     * @return a {@link ResponseEntity} with a success message and HTTP status 200
     */
    @Operation(
            summary = "Delete a task",
            description = "Deletes the task based on the passed title",
            responses = {
                    @ApiResponse(
                            description = "Task deleted!",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/{title}")
    public String deleteTask(@PathVariable @Parameter(description = "Title of the task to be deleted", required = true)
                             @NotBlank @Size(max = 255) String title) {
        service.deleteTaskByTitle(title);
        return "Task deleted!";
    }

    /**
     * Creates a new task.
     *
     * @param taskDto the DTO containing task creation details
     * @return a {@link ResponseEntity} with a success message and HTTP status 201
     */
    @Operation(
            summary = "Create a task",
            description = "Allows you to create a task",
            responses = {
                    @ApiResponse(
                            description = "Task created!",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTask(@Valid @RequestBody TaskDto taskDto) {
        service.createTask(taskDto);
        return "Task created!";
    }

    /**
     * Changes the status of a task.
     *
     * @param title     the title of the task
     * @param newStatus the new status for the task, validated against the {@link Status} enum
     * @return a {@link ResponseEntity} with a success message and HTTP status 200
     */
    @Operation(
            summary = "Change status",
            description = "Allows you to change the status",
            responses = {
                    @ApiResponse(
                            description = "Status has been changed!",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @PatchMapping("/edit/{title}/status")
    @ResponseStatus(HttpStatus.OK)
    public String editStatus(@PathVariable @Parameter(description = "Title of the task to be changed", required = true)
                             @NotBlank @Size(max = 255) String title,
                             @RequestParam @Parameter(description = "New task status {PENDING, IN_PROGRESS, COMPLETED}", required = true)
                             @CheckEnum(enumClass = Status.class) String newStatus) {
        service.editStatus(title, Status.valueOf(newStatus));
        return "Status has been changed!";
    }


    /**
     * Changes the status of a task.
     *
     * @param status the status of the task
     * @return a list of {@link TaskDto} representing the task
     */
    @Operation(
            summary = "Find by status",
            description = "Allows you to find a tasks by status",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Incorrect input data",
                            responseCode = "400"
                    )
            }
    )
    @SecurityRequirement(name = "Bearer Auth")
    @GetMapping("/show/byStatus")
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskDto> editStatus(@RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
                                    @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit,
                                    @RequestParam @Parameter(description = "Task status {PENDING, IN_PROGRESS, COMPLETED}", required = true)
                                    @CheckEnum(enumClass = Status.class) String status) {
        return service.getTasksByStatus(Status.valueOf(status), limit, offset);
    }
}
