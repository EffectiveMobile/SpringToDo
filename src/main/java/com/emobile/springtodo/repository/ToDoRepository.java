package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDoItem;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository {
    List<ToDoItem> findAll(int limit, int offset);

    Optional<ToDoItem> findById(Long id);

    void save(ToDoItem toDoItem);

    void update(ToDoItem toDoItem);

    void deleteById(Long id);
}
