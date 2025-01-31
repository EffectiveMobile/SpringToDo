package com.emobile.springtodo.tasks.controller;

import com.emobile.springtodo.tasks.dto.in.NewTaskRequestDto;
import com.emobile.springtodo.tasks.dto.in.UpdateTaskDto;
import com.emobile.springtodo.tasks.dto.out.TaskResponseDto;
import com.emobile.springtodo.tasks.service.TaskService;
import com.emobile.springtodo.utils.Create;
import com.emobile.springtodo.utils.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "TaskController", description = "Контроллер предоставляющий ручки/handlers для взаимодействие с сущностью Task")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Получение всех записей задач (Task))",
            description = "Позволяет получить все записи из БД"
    )
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> getListOfAllTasks(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {

        return taskService.getListOfAllTasks(from, size);
    }

    @Operation(
            summary = "Получение всех записей задач (Task) по id автора",
            description = "Позволяет получить все записи по id автора из БД"
    )
    @GetMapping("{authorId}/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto getTaskByAuthorIdAndTaskId(@Positive @PathVariable(name = "authorId") Long authorId,
                                                      @Positive @PathVariable(name = "taskId") Long taskId) {

        return taskService.getTaskByAuthorIdAndTaskId(authorId, taskId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTaskByAuthorId(@Validated(Create.class)@RequestBody NewTaskRequestDto newTaskDto) {

        return taskService.createTaskByAuthorId(newTaskDto);
    }

    @PutMapping("/{authorId}/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto updateTaskByAuthorId(@Positive @PathVariable(name = "authorId") Long authorId,
                                                @Positive @PathVariable(name = "taskId") Long taskId,
                                                @Validated(Update.class)@RequestBody UpdateTaskDto updateTaskDto) {

        return taskService.updateTaskByAuthorId(authorId, taskId, updateTaskDto);
    }

    @Operation(
            summary = "Создание новой задачи (Task)",
            description = "Позволяет создать новую (Task)"
    )
    @DeleteMapping("/{authorId}/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TaskResponseDto deleteTaskByAuthorId(@Positive @PathVariable(name = "authorId") Long authorId,
                                                @Positive @PathVariable(name = "taskId") Long taskId) {

        return taskService.deleteTaskByAuthorId(authorId, taskId);
    }
}
