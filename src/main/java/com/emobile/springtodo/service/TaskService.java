package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.Status;


import java.util.List;

public interface TaskService {
    List<TaskDto> getAllTasks(Integer limit, Integer offset);
    List<TaskDto> getTaskByTitle(String title, Integer limit, Integer offset);
    void createTask(TaskDto task);
    void deleteTaskByTitle(String title);
    void editStatus(String title, Status status);
    List<TaskDto> getTasksByStatus(Status status, Integer limit, Integer offset);

}
