package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoItemDto;

import java.util.List;

public interface ToDoService {
    List<ToDoItemDto> getAll(int limit, int offset);

    ToDoItemDto getById(Long id);

    ToDoItemDto create(ToDoItemDto dto);

    ToDoItemDto update(Long id, ToDoItemDto dto);

    void delete(Long id);
}