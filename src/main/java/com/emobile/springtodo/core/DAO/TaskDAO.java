package com.emobile.springtodo.core.DAO;

import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.entity.Task;
import com.emobile.springtodo.core.exception.customexceptions.CategoryNotFoundException;
import com.emobile.springtodo.core.exception.customexceptions.CustomDataAccessException;
import com.emobile.springtodo.core.exception.customexceptions.TaskNotFoundException;
import com.emobile.springtodo.core.repository.TaskRepoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskDAO {

    private final TaskRepoImpl taskRepo;

    private static final String TASK_NOT_FOUND = "Task with ID %s not found";
    private static final String TASKS_BY_FILTER_ERROR = "Error getting tasks for";

    public List<Task> getAll(int limit, int offset) {
        try {
            List<Task> taskList = taskRepo.findAll(limit, offset);
            if (taskList.isEmpty()) {
                throw new TaskNotFoundException("No tasks found");
            }
            return taskList;
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error getting all tasks", e);
        }
    }

    public Task getById(UUID id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        String.format(TASK_NOT_FOUND, id)));
    }

    public List<Task> getByCategory(UUID categoryID) {
        try {
            List<Task> taskList =  taskRepo.findByCategory(categoryID);
            if (taskList.isEmpty()) {
                throw new TaskNotFoundException("No tasks found with Category ID " + categoryID);
            }
            return taskList;
        } catch (DataAccessException e) {
            throw new CustomDataAccessException(String.format(TASKS_BY_FILTER_ERROR + "Category ID %s", categoryID), e);
        }
    }

    public List<Task> getByStatus(String status) {
        try {
            List<Task> taskList =  taskRepo.findByStatus(status);
            if (taskList.isEmpty()) {
                throw new TaskNotFoundException("No tasks found with Status " + status);
            }
            return taskList;
        } catch (DataAccessException e) {
            throw new CustomDataAccessException(String.format(TASKS_BY_FILTER_ERROR + "Status %s", status), e);
        }
    }

    public void createTask(Task task) {
        try {
            int rowsAffected = taskRepo.save(task);
            if (rowsAffected == 0) {
                throw new CustomDataAccessException("Failed to save task: No rows affected");
            }
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error saving task", e);
        }
    }

    public void updateTask(Task task, Long currentVersion) {
        try {
            int rowsAffected = taskRepo.update(task, currentVersion);
            if (rowsAffected == 0) {
                throw new TaskNotFoundException(String.format("Failed to update task: " + TASK_NOT_FOUND, task.getTaskID()));
            }
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error updating task", e);
        }
    }

    public void deleteTask(UUID taskID) {
        try {
            int rowsAffected = taskRepo.delete(taskID);
            if (rowsAffected == 0) {
                throw new TaskNotFoundException(String.format("Failed to delete task: " + TASK_NOT_FOUND, taskID));
            }
        } catch (DataAccessException e) {
            throw new CustomDataAccessException("Error deleting task", e);
        }
    }
}
