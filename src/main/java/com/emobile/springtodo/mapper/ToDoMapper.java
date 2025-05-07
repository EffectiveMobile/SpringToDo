package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.entity.ToDoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(componentModel = "spring")
public interface ToDoMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ToDoEntity toEntity(ToDoDto dto);

    ToDoDto toDto(ToDoEntity entity);
}
