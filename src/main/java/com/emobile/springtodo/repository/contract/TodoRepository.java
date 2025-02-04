package com.emobile.springtodo.repository.contract;

import com.emobile.springtodo.model.Todo;

import java.util.List;

public interface TodoRepository {
    List<Todo> findAll(int limit, int offset);
    Todo findById(Long id);
    Todo save(Todo todo);
    Todo update(Todo todo);
    void delete(Long id);
}
