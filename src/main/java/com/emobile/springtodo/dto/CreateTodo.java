package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.TodoStatus;

/**
 * DTO для создания новой задачи.
 * Содержит данные, необходимые для создания задачи.
 *
 * @author Мельников Никита
 */
public record CreateTodo(String title,
                         String description,
                         TodoStatus status) {
}
