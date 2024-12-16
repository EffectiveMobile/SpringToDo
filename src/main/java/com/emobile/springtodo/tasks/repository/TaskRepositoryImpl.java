package com.emobile.springtodo.tasks.repository;

import com.emobile.springtodo.tasks.dto.in.UpdateTaskDto;
import com.emobile.springtodo.tasks.model.Task;
import com.emobile.springtodo.tasks.model.entity.Priority;
import com.emobile.springtodo.tasks.model.entity.Status;
import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.users.repository.UserRepository;
import com.emobile.springtodo.utils.exception.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;

    @Override
    public List<Task> getListOfAllTasks(Integer from, Integer size) {

    final String sqlQuery = """
                            SELECT *
                            FROM public.tasks
                            """;

        return jdbcTemplate.query(sqlQuery, this::makeTask);
    }

    @Override
    public Task getTaskByAuthorIdAndTaskId(Long authorId, Long taskId) {

        final String checkQuery = """
                                  SELECT *
                                  FROM public.tasks
                                  WHERE tasks.author_id = ?
                                  AND tasks.id = ?
                                  """;

        SqlRowSet taskRows = jdbcTemplate.queryForRowSet(checkQuery, authorId, taskId);

        if (!taskRows.next()) {
            log.warn("Tasks with id: {} not present.", taskId);
            throw new ObjectNotFoundException("Фильм не найден");
        }

        final String sqlQuery = """
                                SELECT *
                                FROM public.tasks
                                WHERE tasks.author_id = ?
                                AND tasks.id = ?
                                """;

        return jdbcTemplate.queryForObject(sqlQuery, this::makeTask, authorId, taskId);
    }

    @Override
    public Task createTaskByAuthorId(Task task) {


        return null;
    }

    @Override
    public Task updateTaskByAuthorId(Long authorId, UpdateTaskDto updateTaskDto) {


        return null;
    }

    @Override
    public Task deleteTaskByAuthorId(Long authorId, Long taskId) {


        return null;
    }

    private Task makeTask(ResultSet resultSet, int rowNum) throws SQLException {

        final Long id = resultSet.getLong("id");
        final LocalDateTime creation = resultSet.getObject("creation", LocalDateTime.class);
        final String description = resultSet.getString("description");
        final String header = resultSet.getString("header");
        final Priority priority = resultSet.getObject("priority", Priority.class);
        final Status status = resultSet.getObject("priority", Status.class);
        final User assignee = this.getUserFromDb(resultSet.getLong("assignee"));
        final User author = this.getUserFromDb(resultSet.getLong("author"));

        return new Task(id, creation,description, header, priority, status, assignee, author);
    }

    private User getUserFromDb(Long userId) {

        return userRepository.getUserById(userId);
    }
}
