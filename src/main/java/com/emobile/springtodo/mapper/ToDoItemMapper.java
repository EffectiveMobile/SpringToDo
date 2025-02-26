package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.entity.ToDoItem;

/**
 * Интерфейс для маппинга между сущностью ToDoItem и DTO ToDoItemDto.
 */
public interface ToDoItemMapper {

    /**
     * Преобразует DTO в сущность.
     *
     * @param dto Объект ToDoItemDto, который нужно преобразовать в сущность.
     * @return Сущность ToDoItem.
     */
    ToDoItem toEntity(ToDoItemDto dto);

    /**
     * Преобразует сущность в DTO.
     * @param entity Объект ToDoItem, который нужно преобразовать в DTO.
     * @return DTO ToDoItemDto.
     */
    ToDoItemDto toDto(ToDoItem entity);
}
