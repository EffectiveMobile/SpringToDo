package com.emobile.springtodo.tasks.repository;

import com.emobile.springtodo.tasks.dto.in.UpdateTaskDto;
import com.emobile.springtodo.tasks.model.Task;
import java.util.List;

public interface TaskRepository {

    List<Task> getListOfAllTasks(Integer from, Integer size);

    Task getTaskByAuthorIdAndTaskId(Long authorId, Long taskId);

    Task createTaskByAuthorId(Task task);

    Task updateTaskByAuthorId(Long authorId, Long taskId, UpdateTaskDto updateTaskDto);

    Task deleteTaskByAuthorId(Long authorId, Long taskId);
}
