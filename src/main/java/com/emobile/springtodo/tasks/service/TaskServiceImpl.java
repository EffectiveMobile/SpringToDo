package com.emobile.springtodo.tasks.service;

import com.emobile.springtodo.tasks.dto.in.NewTaskRequestDto;
import com.emobile.springtodo.tasks.dto.in.UpdateTaskDto;
import com.emobile.springtodo.tasks.dto.out.TaskResponseDto;
import com.emobile.springtodo.tasks.model.Task;
import com.emobile.springtodo.tasks.repository.TaskRepositoryDao;
import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.users.repository.UserRepositoryDao;
import com.emobile.springtodo.utils.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepositoryDao taskRepository;

    private final UserRepositoryDao userRepository;

    @Override
    public List<TaskResponseDto> getListOfAllTasks(Integer from, Integer size) {

        return taskRepository.getListOfAllTasks(from, size)
                .stream()
                .map(TaskMapper::toTaskResponseDto)
                .toList();
    }

    @Override
    public TaskResponseDto getTaskByAuthorIdAndTaskId(Long authorId, Long taskId) {

        return TaskMapper.toTaskResponseDto(taskRepository.getTaskByAuthorIdAndTaskId(authorId, taskId));
    }

    @Override
    public TaskResponseDto createTaskByAuthorId(NewTaskRequestDto newTaskDto) {

        User author = this.getUserFromDb(newTaskDto.author());
        User assignee = this.getUserFromDb(newTaskDto.assignee());

        Task task = Task
                .builder()
                .creation(LocalDateTime.now())
                .description(newTaskDto.description())
                .header(newTaskDto.header())
                .priority(newTaskDto.priority())
                .status(newTaskDto.status())
                .author(author)
                .assignee(assignee)
                .build();

        return TaskMapper.toTaskResponseDto(taskRepository.createTaskByAuthorId(task));
    }

    @Override
    public TaskResponseDto updateTaskByAuthorId(Long authorId, Long taskId, UpdateTaskDto updateTaskDto) {

        Task task = taskRepository.getTaskByAuthorIdAndTaskId(authorId, taskId);

        if (updateTaskDto.header() != null) {
            task.setHeader(updateTaskDto.header());
        }

        if (updateTaskDto.description() != null) {
            task.setDescription(updateTaskDto.description());
        }

        if (updateTaskDto.status() != null) {
            task.setStatus(updateTaskDto.status());
        }

        if (updateTaskDto.assignee() != null) {

            User assignee = userRepository.getUserById(updateTaskDto.assignee());
            task.setAssignee(assignee);
        }

        return TaskMapper.toTaskResponseDto(
                taskRepository.update(task));
    }

    @Override
    public TaskResponseDto deleteTaskByAuthorId(Long authorId, Long taskId) {


        return TaskMapper.toTaskResponseDto(taskRepository.deleteTaskByAuthorId(authorId, taskId));
    }

    private User getUserFromDb(Long userId) {

        return userRepository.getUserById(userId);
    }
}
