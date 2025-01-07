package emobile.by.smertex.springtodo.database.repository.sql.interfaces;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Optional<Task> findById(UUID id);

    List<Task> findAllByFilter(TaskFilter filter, SecurityUserDto user, Pageable pageable);

    Task save(Task task);

    Task update(Task task);

    void delete(Task task);
}
