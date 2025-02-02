package com.emobile.springtodo.services;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.entities.Task;
import com.emobile.springtodo.exceptions.EntityNotFoundException;
import com.emobile.springtodo.mappers.TaskMapper;
import com.emobile.springtodo.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.emobile.springtodo.exceptions.DescriptionUserExeption.TASK_NOT_FOUND_BY_ID;

@Service
public class TaskServicesImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServicesImpl(TaskMapper taskMapper, TaskRepository taskRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskDto createTasks(TaskDto tasksDto) {
        Task task = taskMapper.convertDtoToTasks(tasksDto);
        task.setId(null);
        task = taskRepository.save(task);
        return taskMapper.convertTasksToDto(task);
    }

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
        return Optional.of(taskMapper.convertTasksToDto(taskFromDB));
    }

    @Override
    public Optional<List<TaskDto>> getTasksByTitle(String tasksName, Integer offset, Integer limit) {
        Optional<List<Task>> optionalTaskList = taskRepository.findAllByTitle(tasksName, offset, limit);
        if (optionalTaskList.isPresent()) {
            return Optional.of(taskMapper.transferListTasksToDto(optionalTaskList.get()));
        }
        return Optional.empty();
    }

    @Override
    public void deleteTasks(Long idTasks) {
        taskRepository.deleteById(idTasks);
    }
}
