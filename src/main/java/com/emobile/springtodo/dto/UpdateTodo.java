package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.TodoStatus;

/**
 * DTO для обновления существующей задачи.
 * Содержит данные, которые можно изменить в задаче.
 *
 * @author Мельников Никита
 */
public record UpdateTodo(String title,
                         String description,
                         TodoStatus status) {
}
