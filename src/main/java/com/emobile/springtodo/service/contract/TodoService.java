package com.emobile.springtodo.service.contract;

import com.emobile.springtodo.dto.CreateTodo;
import com.emobile.springtodo.dto.UpdateTodo;
import com.emobile.springtodo.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> getAllTodos(int limit, int offset);
    Todo getTodoById(Long id);
    Todo saveTodo(CreateTodo createTodo);
    Todo updateTodo(Long id, UpdateTodo updatedTodo);
    void deleteTodo(Long id);
}
