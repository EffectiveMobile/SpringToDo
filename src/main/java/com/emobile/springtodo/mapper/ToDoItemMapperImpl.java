package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.entity.ToDoItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Реализация маппера для преобразования сущности ToDoItem в DTO и наоборот.
 */
@Component
public class ToDoItemMapperImpl implements ToDoItemMapper {

    /**
     * Преобразует DTO в сущность ToDoItem.
     *
     * @param dto DTO для преобразования.
     * @return Сущность ToDoItem.
     */
    @Override
    public ToDoItem toEntity(ToDoItemDto dto) {
        return new ToDoItem(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.isCompleted(),
                LocalDateTime.now()
        );
    }

    /**
     * Преобразует сущность ToDoItem в DTO.
     * @param entity Сущность ToDoItem для преобразования.
     * @return DTO ToDoItemDto.
     */
    @Override
    public ToDoItemDto toDto(ToDoItem entity) {
        return ToDoItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .completed(entity.isCompleted())
                .build();
    }
}
