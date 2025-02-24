package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.entity.ToDoItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ToDoItemMapperImpl implements ToDoItemMapper {

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
