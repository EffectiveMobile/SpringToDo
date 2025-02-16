package com.emobile.springtodo.service.impl;

import com.emobile.springtodo.dto.CreateTodo;
import com.emobile.springtodo.dto.UpdateTodo;
import com.emobile.springtodo.exception.InvalidDataException;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.repository.contract.TodoRepository;
import com.emobile.springtodo.service.contract.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    @Cacheable(value = "todos", key = "'all-' + #limit + '-' + #offset")
    public List<Todo> getAllTodos(int limit, int offset) {
        log.info("No data in cache, going to DB...");
        return todoRepository.findAll(limit, offset);
    }

    @Override
    @Cacheable(value = "todos", key = "#id")
    public Todo getTodoById(Long id) {
        log.info("No data in cache, going to DB...");
        Todo todo = todoRepository.findById(id);
        if (todo == null) {
            throw new TodoNotFoundException(id);
        }
        return todo;
    }

    @Override
    public Todo saveTodo(CreateTodo createTodo) {
        if (createTodo.title() == null || createTodo.title().trim().isEmpty()) {
            throw new InvalidDataException("Title cannot be empty or null");
        }
        Todo todo = new Todo();
        todo.setTitle(createTodo.title());
        todo.setDescription(createTodo.description());
        todo.setStatus(createTodo.status() != null ? createTodo.status() : TodoStatus.TO_DO);
        return todoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(Long id, UpdateTodo updateTodo) {
        Todo existingTodo = todoRepository.findById(id);
        if (existingTodo == null) {
            throw new TodoNotFoundException(id);
        }

        if (updateTodo.title() != null && !updateTodo.title().trim().isEmpty()) {
            existingTodo.setTitle(updateTodo.title());
        } else {
            throw new InvalidDataException("Title cannot be empty or null");
        }

        if (updateTodo.description() != null) {
            existingTodo.setDescription(updateTodo.description());
        }

        existingTodo.setStatus(updateTodo.status());

        return todoRepository.update(existingTodo);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.delete(id);
    }
}
