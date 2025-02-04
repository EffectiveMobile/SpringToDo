package com.emobile.springtodo.services;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.entities.Task;
import com.emobile.springtodo.exceptions.DataCalendarNotBeNullException;
import com.emobile.springtodo.exceptions.EntityNotFoundException;
import com.emobile.springtodo.exceptions.StatusTaskNotBeNullException;
import com.emobile.springtodo.mappers.TaskMapper;
import com.emobile.springtodo.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.emobile.springtodo.exceptions.DescriptionUserExeption.*;
import static com.emobile.springtodo.other.ConstantsClass.*;

@Service
@Slf4j
public class TaskServicesImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServicesImpl(TaskMapper taskMapper, TaskRepository taskRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    @CachePut(cacheNames = "taskServiceCache", key = "#result.id")
    @Transactional
    @Override
    public TaskDto createTasks(TaskDto tasksDto) throws StatusTaskNotBeNullException, DataCalendarNotBeNullException {
        if (tasksDto.getTitle() == null) {
            throw new StatusTaskNotBeNullException(STATUS_NOT_BE_NULL.getEnumDescription());
        } else if (tasksDto.getDateCalendar() == null) {
            throw new DataCalendarNotBeNullException(DATE_CALENDAR_NOT_BE_NULL.getEnumDescription());
        }
        Task task = taskMapper.convertDtoToTasks(tasksDto);
        task.setId(null);
        task = taskRepository.save(task);
        log.info("create");
        return taskMapper.convertTasksToDto(task);
    }

    @CachePut(cacheNames = "taskServiceCache", key = "#tasksDto.id")
    @Transactional
    @Override
    public Optional<TaskDto> changeTasks(TaskDto tasksDto) throws EntityNotFoundException {
        Task taskFromDB = taskRepository.findById(tasksDto.getId());
        if (taskFromDB == null) {
            throw new EntityNotFoundException(TASK_NOT_FOUND_BY_ID.getEnumDescription());
        }
        taskMapper.compareTaskAndDto(tasksDto, taskFromDB);
        taskRepository.update(taskFromDB);
        if (taskFromDB.getId() == null) {
            return Optional.empty();
        }
        log.info("change");
        return Optional.of(taskMapper.convertTasksToDto(taskFromDB));
    }

    @Cacheable(cacheNames = "taskServiceCache", key = "#result.?[].id")
    @Transactional
    @Override
    public Optional<List<TaskDto>> getTasksByTitle(String taskTitle, Integer offset, Integer limit) {
        StringBuffer localStringBuffer = new StringBuffer(taskTitle);
        char[] chars = taskTitle.toCharArray();
        if (chars[ZERO] == PERCENT_CHAR_PRIMITIVE) {
            localStringBuffer.insert(ZERO, PERCENT);
        }
        if (chars[chars.length - ONE] == PERCENT_CHAR_PRIMITIVE) {
            localStringBuffer.append(PERCENT);
        }
        Optional<List<Task>> optionalTaskList = taskRepository.findAllByTitle(localStringBuffer.toString(), offset, limit);
        if (optionalTaskList.isPresent() && !optionalTaskList.get().isEmpty()) {
            return Optional.of(taskMapper.transferListTasksToDto(optionalTaskList.get()));
        }
        log.info("get");
        return Optional.empty();
    }

    @CacheEvict(cacheNames = "taskServiceCache", key = "#idTasks")
    @Transactional
    @Override
    public boolean deleteTasks(Long idTasks) {
        boolean taskExist = taskRepository.existsById(idTasks);
        if (taskExist) {
            taskRepository.deleteById(idTasks);
            log.info("delete");
            return true;
        }
        log.info("delete");
        return false;
    }
}
