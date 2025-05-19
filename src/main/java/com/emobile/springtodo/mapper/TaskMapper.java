package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting between Task-related DTOs and Entities.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    /**
     * Converts  a {@link TaskEntity} to a {@link TaskDto}.
     *
     * @param entity the task entity to be mapped.
     * @return a {@link TaskDto} with the mapped and computed fields.
     */
    TaskDto toDto(TaskEntity entity);

    /**
     * Converts a {@link TaskDto} to a {@link TaskEntity}.
     *
     * @param dto the DTO containing the task creation data.
     * @return a {@link TaskEntity} with the mapped fields.
     */
    TaskEntity toEntity(TaskDto dto);

    /**
     * Converts a list of {@link TaskEntity} objects to a list of {@link TaskDto} objects.
     *
     * @param entities the list of task entities to be mapped.
     * @return a list of {@link TaskDto} objects.
     */
    List<TaskDto> toDtoList(List<TaskEntity> entities);
}
