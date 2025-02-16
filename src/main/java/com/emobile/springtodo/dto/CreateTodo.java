package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.TodoStatus;

public record CreateTodo (String title, String description, TodoStatus status) {
}
