package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.CreateTodo;
import com.emobile.springtodo.dto.ErrorResponse;
import com.emobile.springtodo.dto.UpdateTodo;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.service.contract.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todo API", description = "Операции для управления задачами")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Получить все задачи", description = "Возвращает список задач с пагинацией")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Todo.class)))
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Todo> todos = todoService.getAllTodos(limit, offset);
        return ResponseEntity.ok(todos);
    }

    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по её уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Задача найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Todo.class)))
    @ApiResponse(responseCode = "404", description = "Задача не найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    @Operation(summary = "Создать новую задачу", description = "Создаёт новую задачу")
    @ApiResponse(responseCode = "201", description = "Задача успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Todo.class)))
    @ApiResponse(responseCode = "400", description = "Переданы неправильные данные",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Todo> saveTodo(@RequestBody CreateTodo createTodo) {
        Todo createdTodo = todoService.saveTodo(createTodo);
        return ResponseEntity.status(201).body(createdTodo);
    }

    @Operation(summary = "Обновить задачу", description = "Обновляет существующую задачу")
    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Todo.class)))
    @ApiResponse(responseCode = "400", description = "Переданы неправильные данные",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Задача не найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody UpdateTodo updateTodo) {
        Todo updatedTodo = todoService.updateTodo(id, updateTodo);
        return ResponseEntity.ok(updatedTodo);
    }

    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по её уникальному идентификатору")
    @ApiResponse(responseCode = "204", description = "Задача успешно удалена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
