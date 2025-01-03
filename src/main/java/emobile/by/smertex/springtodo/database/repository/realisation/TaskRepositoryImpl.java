package emobile.by.smertex.springtodo.database.repository.realisation;

import emobile.by.smertex.springtodo.database.entity.realisation.Task;
import emobile.by.smertex.springtodo.database.repository.interfaces.TaskRepository;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByFilter(TaskFilter filter, SecurityUserDto user, Pageable pageable) {
        return List.of();
    }

    @Override
    public Task save(Task task) {
        return null;
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public void delete(Task task) {

    }
}
