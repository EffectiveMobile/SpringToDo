package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.Status;
import org.springframework.data.domain.Page;


public interface TaskService {
    Page<TaskDto> getAllTasks(Integer limit, Integer offset);

    Page<TaskDto> getTaskByTitle(String title, Integer limit, Integer offset);

    void createTask(TaskDto task);

    void deleteTaskByTitle(String title);

    void editStatus(String title, Status status);

    Page<TaskDto> getTasksByStatus(Status status, Integer limit, Integer offset);

}