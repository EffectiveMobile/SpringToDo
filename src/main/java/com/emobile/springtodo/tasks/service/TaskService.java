package com.emobile.springtodo.tasks.service;

import com.emobile.springtodo.tasks.dto.in.NewTaskRequestDto;
import com.emobile.springtodo.tasks.dto.in.UpdateTaskDto;
import com.emobile.springtodo.tasks.dto.out.TaskResponseDto;
import java.util.List;

public interface TaskService {

    List<TaskResponseDto> getListOfAllTasks(Integer from, Integer size);

    TaskResponseDto getTaskByAuthorIdAndTaskId(Long authorId, Long taskId);

    TaskResponseDto createTaskByAuthorId(NewTaskRequestDto newTaskDto);

    TaskResponseDto updateTaskByAuthorId(Long authorId, Long taskId, UpdateTaskDto updateTaskDto);

    TaskResponseDto deleteTaskByAuthorId(Long authorId, Long taskId);
}
