package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.service.ToDoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "ToDo API", description = "API для управления задачами ToDo")
public class ToDoController {

    private final ToDoService toDoService;

    @GetMapping
    @Operation(summary = "Получить все задачи", description = "Получает список всех задач с возможностью пагинации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка задач"),
            @ApiResponse(responseCode = "400", description = "Ошибка пагинации")
    })
    public List<ToDoItemDto> getAll(@RequestParam(defaultValue = "10") int limit,
                                    @RequestParam(defaultValue = "0") int offset) {
        return toDoService.getAll(limit, offset);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Получает задачу по ее уникальному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно получена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ToDoItemDto getById(@PathVariable Long id) {
        return toDoService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новую задачу", description = "Создает новую задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных")
    })
    public ResponseEntity<ToDoItemDto> create(@Valid @RequestBody ToDoItemDto dto) {
        return ResponseEntity.ok(toDoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу", description = "Обновляет существующую задачу по ее ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<ToDoItemDto> update(@PathVariable Long id, @Valid @RequestBody ToDoItemDto dto) {
        return ResponseEntity.ok(toDoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по ее ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        toDoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
