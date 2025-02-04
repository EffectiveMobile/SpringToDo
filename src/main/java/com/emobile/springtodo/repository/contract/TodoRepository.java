package com.emobile.springtodo.repository.contract;

import com.emobile.springtodo.model.Todo;

import java.util.List;

public interface TodoRepository {
    List<Todo> findAll(int limit, int offset);
    Todo findById(Long id);
    void save(Todo todo);
    void update(Todo todo);
    void delete(Long id);
}
