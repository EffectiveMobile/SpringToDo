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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    private final UserRepository userRepository;

    private static final String QUERY_SELECT = """
                                               SELECT *
                                               FROM public.tasks
                                               WHERE tasks.author_id = ?
                                               AND tasks.id = ?
                                               """;

    @Override
    public List<Task> getListOfAllTasks(Integer from, Integer size) {

    final String sqlQuery = """
                            SELECT *
                            FROM public.tasks
                            OFFSET ?
                            LIMIT ?
                            """;

        return jdbcTemplate.query(sqlQuery, this::makeTask, from, size);
    }

    @Override
    public Task getTaskByAuthorIdAndTaskId(Long authorId, Long taskId) {

        SqlRowSet taskRows = jdbcTemplate.queryForRowSet(QUERY_SELECT, authorId, taskId);

        if (!taskRows.next()) {
            log.warn("Tasks with id: {} not present.", taskId);
            throw new ObjectNotFoundException("Фильм не найден");
        }

        return jdbcTemplate.queryForObject(QUERY_SELECT, this::makeTask, authorId, taskId);
    }

    @Override
    public Task createTaskByAuthorId(Task task) {

        final String sqlQuery = """
                                INSERT INTO tasks (creation, description, header, priority, status, assignee_id, author_id)
                                VALUES(?, ?, ?, ?, ?, ?, ?)
                                """;

        KeyHolder generatedId = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setDate(1, Date.valueOf(String.valueOf(task.getCreation())));
            stmt.setString(2, task.getDescription());
            stmt.setString(3,task.getHeader());
            stmt.setString(4, String.valueOf(task.getPriority()));
            stmt.setString(5, String.valueOf(task.getStatus()));
            stmt.setLong(6, task.getAssignee().getId());
            stmt.setLong(7, task.getAuthor().getId());

            return stmt;
        }, generatedId);

        task.setId(Objects.requireNonNull(generatedId.getKey()).longValue());

        return task;
    }

    @Override
    public Task updateTaskByAuthorId(Long authorId, Long taskId, UpdateTaskDto updateTaskDto) {

        SqlRowSet taskRows = jdbcTemplate.queryForRowSet(QUERY_SELECT, authorId, taskId);

        if (!taskRows.next()) {
            log.warn("Task with id: {} was not present", taskId);
            throw new ObjectNotFoundException("Task was not present");
        }

        final String sqlQuery= """
                               UPDATE tasks SET description = ?, header = ?, priority = ?, status = ?, assignee_id = ?
                               WHERE tasks.author_id = ?
                               AND tasks.id = ?
                               """;

        jdbcTemplate.update(sqlQuery, updateTaskDto.description(), updateTaskDto.header(), updateTaskDto.priority(),
                updateTaskDto.status(), updateTaskDto.assignee());

        return this.getTaskByAuthorIdAndTaskId(authorId, taskId);
    }

    @Override
    public Task deleteTaskByAuthorId(Long authorId, Long taskId) {

        final Task task = this.getTaskByAuthorIdAndTaskId(authorId, taskId);

        final String sqlQuery = """
                                DELETE
                                FROM public.tasks
                                WHERE tasks.author_id = ?
                                AND tasks.id = ?
                                """;
        jdbcTemplate.update(sqlQuery, authorId, taskId);

        return task;
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
