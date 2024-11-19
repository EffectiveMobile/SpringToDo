package com.emobile.springtodo.core.service;

import com.emobile.springtodo.api.request.TaskCreationRequest;
import com.emobile.springtodo.api.request.TaskUpdateRequest;
import com.emobile.springtodo.core.DAO.CategoryDAO;
import com.emobile.springtodo.core.DAO.TaskDAO;
import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.entity.Status;
import com.emobile.springtodo.core.entity.Task;
import com.emobile.springtodo.core.exception.customexceptions.TaskCreationException;
import com.emobile.springtodo.core.exception.customexceptions.TaskDeletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskDAO taskDAO;
    private final CategoryDAO categoryDAO;

    private static final String CACHE_ONE_ENTRY = "task";
    private static final String CACHE_LIST_ENTRIES = "task_collection";

    public List<Task> getAllTasks(int limit, int offset) {
        log.debug("Getting tasks with limit {} and offset {}", limit, offset);

        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be negative");
        }

        return taskDAO.getAll(limit, offset);
    }

    @Cacheable(value = CACHE_ONE_ENTRY, key = "#taskID", unless = "#result == null")
    public Task getTaskById(String taskID) {
        log.debug("Getting task with ID {}", taskID);
        return taskDAO.getById(UUID.fromString(taskID));
    }

    @Cacheable(value = CACHE_LIST_ENTRIES, key = "'category:' + #categoryID", unless = "#result.isEmpty()")
    public List<Task> getByCategory(String categoryID) {
        log.debug("Getting tasks by category {}", categoryID);

        Category category = categoryDAO.getById(UUID.fromString(categoryID));

        return taskDAO.getByCategory(category.getCategoryID());
    }

    @Cacheable(value = CACHE_LIST_ENTRIES, key = "'status:' + #status", unless = "#result.isEmpty()")
    public List<Task> getByStatus(String status) {
        log.debug("Getting tasks by status {}", status);

        checkStatus(status);

        return taskDAO.getByStatus(status);
    }

    @Transactional
    @CachePut(value = CACHE_ONE_ENTRY, key = "#result.taskID", unless = "#result == null")
    public Task createTask(TaskCreationRequest request) throws TaskCreationException {
        log.debug("Creating new task with title: {}", request.taskTitle());

        checkStatus(request.taskStatus());

        Category category = categoryDAO.getById(UUID.fromString(request.taskCategory()));

        Task task = Task.builder()
                .taskID(UUID.randomUUID())
                .taskTitle(request.taskTitle())
                .taskDescription(request.taskDescription() != null ? request.taskDescription() : "")
                .taskStatus(Status.valueOf(request.taskStatus().toUpperCase()))
                .taskCategory(category.getCategoryID())
                .taskDate(request.taskDate())
                .taskHours(request.taskHours() != null ? request.taskHours() : 0)
                .build();

        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setVersion(1L);

        try {
            taskDAO.createTask(task);
            return task;
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to create task due to data integrity violation", e);
            throw new TaskCreationException("Failed to create task: " + e.getMessage());
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_LIST_ENTRIES, key = "'category:' + #result.taskCategory", condition = "#result != null"),
                    @CacheEvict(value = CACHE_LIST_ENTRIES, key = "'status:' + #result.taskStatus", condition = "#result != null")},

            put = {
                    @CachePut(value = CACHE_ONE_ENTRY, key = "#taskID", unless = "#result == null")
            }
    )
    @Transactional
    public Task updateTask(String taskID, TaskUpdateRequest request) {
        log.debug("Updating task with ID: {}", taskID);

        Task task = taskDAO.getById(UUID.fromString(taskID));

        Category category;
        if (request.taskCategory() != null && !request.taskCategory().isEmpty()) {
            category = categoryDAO.getById(UUID.fromString(request.taskCategory()));
        } else {
            category = categoryDAO.getById(task.getTaskCategory());
        }

        Status status;
        if (request.taskStatus() !=null && !request.taskStatus().isEmpty()) {
            checkStatus(request.taskStatus());
            status = Status.valueOf(request.taskStatus());
        } else {
            status = task.getTaskStatus();
        }

        Task updatedTask = Task.builder()
                .taskID(task.getTaskID())
                .taskTitle(request.taskTitle() != null ? request.taskTitle() : task.getTaskTitle())
                .taskDescription(request.taskDescription() != null ? request.taskDescription() : task.getTaskDescription())
                .taskStatus(status)
                .taskCategory(category.getCategoryID())
                .taskDate(request.taskDate() != null ? request.taskDate() : task.getTaskDate())
                .taskHours(request.taskHours() != null ? request.taskHours() : task.getTaskHours())
                .build();

        updatedTask.setCreatedAt(task.getCreatedAt());
        updatedTask.setUpdatedAt(LocalDateTime.now());

        Long currentVersion = task.getVersion();
        updatedTask.setVersion(task.getVersion() + 1);

        try {
            taskDAO.updateTask(updatedTask, currentVersion);
            return updatedTask;
        } catch (OptimisticLockingFailureException e) {
            log.error("Concurrent modification detected for task: {}", taskID, e);
            throw new ConcurrentModificationException("Task was modified by another transaction");
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_ONE_ENTRY, key = "#taskID"),
            @CacheEvict(value = CACHE_LIST_ENTRIES, key = "'category:' + #result.taskCategory", condition = "#result != null"),
            @CacheEvict(value = CACHE_LIST_ENTRIES, key = "'status:' + #result.taskStatus", condition = "#result != null")
    })
    public Task deleteTask(String taskID) throws TaskDeletionException {
        log.debug("Deleting task with ID {}", taskID);

        Task task = taskDAO.getById(UUID.fromString(taskID));

        try {
            taskDAO.deleteTask(task.getTaskID());
            return task;
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete task due to data integrity violation", e);
            throw new TaskDeletionException("Failed to delete task: " + e.getMessage());
        }
    }

    private void checkStatus(String status){
        log.debug("Validate status {}", status);
        try {
            Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
