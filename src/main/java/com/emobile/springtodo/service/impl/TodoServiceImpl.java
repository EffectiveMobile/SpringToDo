package com.emobile.springtodo.service.impl;

import com.emobile.springtodo.dto.CreateTodo;
import com.emobile.springtodo.dto.UpdateTodo;
import com.emobile.springtodo.exception.InvalidDataException;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.repository.contract.TodoRepository;
import com.emobile.springtodo.service.contract.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    @Cacheable(value = "todos", key = "'all-' + #limit + '-' + #offset")
    public List<Todo> getAllTodos(int limit, int offset) {
        return todoRepository.findAll(limit, offset);
    }

    @Override
    @Cacheable(value = "todos", key = "'id-' + #id")
    public Todo getTodoById(Long id) {
        Todo todo = todoRepository.findById(id);
        if (todo == null) {
            throw new TodoNotFoundException(id);
        }
        return todo;
    }

    @Override
    public Todo saveTodo(CreateTodo createTodo) {
        if (createTodo.getTitle() == null || createTodo.getTitle().trim().isEmpty()) {
            throw new InvalidDataException("Title cannot be empty or null");
        }
        if (!TodoStatus.isValid(createTodo.getStatus())) {
            throw new InvalidDataException("Status must be: " + Arrays.toString(TodoStatus.values()));
        }
        Todo todo = new Todo();
        todo.setTitle(createTodo.getTitle());
        todo.setDescription(createTodo.getDescription());
        todo.setStatus(createTodo.getStatus() != null ? createTodo.getStatus() : TodoStatus.TO_DO);
        return todoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(Long id, UpdateTodo updateTodo) {
        Todo existingTodo = todoRepository.findById(id);
        if (existingTodo == null) {
            throw new TodoNotFoundException(id);
        }

        if (updateTodo.getTitle() != null && !updateTodo.getTitle().trim().isEmpty()) {
            existingTodo.setTitle(updateTodo.getTitle());
        } else {
            throw new InvalidDataException("Title cannot be empty or null");
        }

        if (updateTodo.getDescription() != null) {
            existingTodo.setDescription(updateTodo.getDescription());
        }

        if (TodoStatus.isValid(updateTodo.getStatus())) {
            existingTodo.setStatus(updateTodo.getStatus());
        } else {
            throw new InvalidDataException("Status must be: " + Arrays.toString(TodoStatus.values()));
        }

        return todoRepository.update(existingTodo);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.delete(id);
    }
}
