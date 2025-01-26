package com.emobile.springtodo.tasks.repository;

import com.emobile.springtodo.tasks.model.Task;
import java.util.List;

public interface TaskRepositoryDao {

    List<Task> getListOfAllTasks(Integer from, Integer size);

    Task getTaskByAuthorIdAndTaskId(Long authorId, Long taskId);

    Task createTaskByAuthorId(Task task);

    Task update(Task updateTaskDto);

    Task deleteTaskByAuthorId(Long authorId, Long taskId);
}
