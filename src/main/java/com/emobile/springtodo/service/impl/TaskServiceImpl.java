package com.emobile.springtodo.service.impl;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.exceptions.ErrorInputDataException;
import com.emobile.springtodo.mapper.TaskMapper;
import com.emobile.springtodo.model.Status;
import com.emobile.springtodo.model.TaskEntity;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@CacheConfig(cacheNames = "tasks")
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskServiceImpl(TaskRepository repository, TaskMapper taskMapper) {
        this.repository = repository;
        this.mapper = taskMapper;
    }


    @Override
    @Cacheable(key = "{#limit, #offset}", unless = "#result == null or #result.isEmpty()")
    public Page<TaskDto> getAllTasks(Integer limit, Integer offset) {
        List<TaskEntity> taskEntities = repository.findAllTask(limit, offset);
        return new PageImpl<>(mapper.toDtoList(taskEntities), PageRequest.of(offset, limit), taskEntities.size());
    }


    @Override
    @Cacheable(key = "{#title, #limit, #offset}", unless = "#result == null or #result.isEmpty()")
    public Page<TaskDto> getTaskByTitle(String title, Integer limit, Integer offset) {
        List<TaskEntity> taskEntities = repository.findTaskByTitle(title, limit, offset);
        return new PageImpl<>(mapper.toDtoList(taskEntities), PageRequest.of(offset, limit), taskEntities.size());
    }

    @Override
    @Cacheable(key = "{#status, #limit, #offset}", unless = "#result == null or #result.isEmpty()")
    public Page<TaskDto> getTasksByStatus(Status status, Integer limit, Integer offset) {
        List<TaskEntity> taskEntities = repository.findTaskByStatus(status.toString(), limit, offset);
        return new PageImpl<>(mapper.toDtoList(taskEntities), PageRequest.of(offset, limit), taskEntities.size());
    }


    @Override
    @CacheEvict(allEntries = true)
    public void createTask(TaskDto task) {
        if (repository.existTaskByTitle(task.getTitle())) {
            log.error("Task '{}' already exist", task.getTitle());
            throw new ErrorInputDataException("Task " + task.getTitle() + " already exist");
        }
        TaskEntity taskEntity = mapper.toEntity(task);
        repository.createTask(taskEntity);
        log.info("Success create Task '{}'", task.getTitle());
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteTaskByTitle(String title) {
        if (!repository.existTaskByTitle(title)) {
            log.error("Task with title '{}' not found", title);
            throw new ErrorInputDataException("Task with title '" + title + "' not found");
        }
        repository.deleteTaskByTitle(title);
        log.info("Success delete Task '{}'", title);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void editStatus(String title, Status status) {
        if (!repository.existTaskByTitle(title)) {
            log.error("Task with title '{}' not found", title);
            throw new ErrorInputDataException("Task with title '" + title + "' not found");
        }
        repository.editStatus(title, status);
        log.info("Task '{}' status updated to '{}'", title, status);
    }

}
