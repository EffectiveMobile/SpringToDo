package com.emobile.springtodo.utils.mappers;

import com.emobile.springtodo.tasks.dto.out.TaskResponseDto;
import com.emobile.springtodo.tasks.model.Task;
import com.emobile.springtodo.utils.exception.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskMapper {

    private TaskMapper() {
        log.info("Someone tries to crate exemplar of TaskMapper Class!");
        throw new BadRequestException("This class is utility for purposes only!");
    }

    public static TaskResponseDto toTaskResponseDto(Task taskFromDb) {

        return new TaskResponseDto(taskFromDb.getId(),
                                    taskFromDb.getCreation(),
                                    taskFromDb.getDescription(),
                                    taskFromDb.getHeader(), taskFromDb.getPriority(), taskFromDb.getStatus(),
                                    UserMapper.toUserResponseDto(taskFromDb.getAssignee()),
                                    UserMapper.toUserResponseDto(taskFromDb.getAuthor()));
    }
}