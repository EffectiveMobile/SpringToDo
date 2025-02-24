package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.entity.ToDoItem;

public interface ToDoItemMapper {
    ToDoItem toEntity(ToDoItemDto dto);

    ToDoItemDto toDto(ToDoItem entity);
}
