package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.service.exception.SaveMetainfoException;
import emobile.by.smertex.springtodo.service.exception.UserNotFoundInDatabaseException;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.database.repository.sql.TaskRepository;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.read.ReadTaskDto;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateTaskDto;
import emobile.by.smertex.springtodo.mapper.realisation.CreateOrUpdateTaskDtoToTaskMapper;
import emobile.by.smertex.springtodo.mapper.realisation.TaskToReadTaskDtoMapper;
import emobile.by.smertex.springtodo.service.exception.SaveException;
import emobile.by.smertex.springtodo.service.exception.UpdateException;
import emobile.by.smertex.springtodo.service.interfaces.AuthService;
import emobile.by.smertex.springtodo.service.interfaces.TaskService;
import emobile.by.smertex.springtodo.service.interfaces.UserService;
import emobile.by.smertex.springtodo.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;

    private final TaskToReadTaskDtoMapper taskToReadTaskDtoMapper;

    private final CreateOrUpdateTaskDtoToTaskMapper createOrUpdateTaskDtoToTaskMapper;

    private final MetainfoServiceImpl metainfoServiceImpl;

    private final AuthService authService;

    private final UserService userService;

    public Optional<Task> findById(UUID id){
        return taskRepository.findById(id);
    }

    @Override
    public List<ReadTaskDto> findAllByFilter(TaskFilter filter, Pageable pageable){
        return taskRepository.findAllByFilter(filter, authService.takeUserFromContext().orElseThrow(), pageable)
                .stream()
                .map(taskToReadTaskDtoMapper::map)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = {UserNotFoundInDatabaseException.class, SaveMetainfoException.class})
    public ReadTaskDto save(CreateOrUpdateTaskDto dto) {
        return Optional.of(dto)
                .map(element -> {
                    Task task = createOrUpdateTaskDtoToTaskMapper.map(element);
                    task.setMetainfo(metainfoServiceImpl.save()
                            .orElseThrow(() -> new SaveMetainfoException(ResponseMessage.SAVE_METAINFO_FAILED)));
                    task.setPerformer(userService.findByEmail(dto.performerEmail())
                            .orElseThrow(() -> new UserNotFoundInDatabaseException(dto.performerEmail())));
                    return task;
                })
                .map(taskRepository::save)
                .map(taskToReadTaskDtoMapper::map)
                .orElseThrow(() -> new SaveException(ResponseMessage.CREATE_TASK_EXCEPTION));
    }

    @Override
    @Transactional
    public ReadTaskDto update(UUID id, CreateOrUpdateTaskDto dto) {
        return taskRepository.findById(id)
                .filter(this::hasAccess)
                .map(task -> {
                    Task update = createOrUpdateTaskDtoToTaskMapper.map(dto, task);
                    update.setPerformer(userService.findByEmail(dto.performerEmail())
                            .orElseThrow(() -> new UserNotFoundInDatabaseException(dto.performerEmail())));
                    update.getMetainfo().setUpdatedAt(LocalDateTime.now());
                    return update;
                })
                .map(task -> {
                    taskRepository.update(task);
                    metainfoServiceImpl.update(task.getMetainfo());
                    return task;
                })
                .map(taskToReadTaskDtoMapper::map)
                .orElseThrow(() -> new UpdateException(ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @Override
    @Transactional
    public boolean delete(UUID id){
        if(!authService.takeUserFromContext().orElseThrow().isAdmin())
            return false;
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                })
                .orElse(false);
    }

    private boolean hasAccess(Task task){
        SecurityUserDto securityUserDto = authService.takeUserFromContext()
                .orElseThrow();
        return task.getPerformer().getEmail().equals(securityUserDto.email()) || securityUserDto.isAdmin();
    }
}
