package com.emobile.springtodo.core.repository;

import com.emobile.springtodo.core.DTO.TaskMapper;
import com.emobile.springtodo.core.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepoImpl implements TaskRepo {

    private final NamedParameterJdbcTemplate template;
    private static final RowMapper<Task> TASK_MAPPER = new TaskMapper();

    private static final String BASE_SELECT = """
            SELECT task_id, title, description,
                   status, date, hours, category_id,
                   created_at, updated_at, version
           FROM task
           """;

    private static final String FIND_ALL = BASE_SELECT  + " LIMIT :limit OFFSET :offset";

    private static final String FIND_BY_ID = BASE_SELECT + "WHERE task_id = :taskId";

    private static final String FIND_BY_CATEGORY_ID = BASE_SELECT + "WHERE category_id = :categoryId";

    private static final String FIND_BY_STATUS = BASE_SELECT + "WHERE status = :status";

    private static final String SAVE_TASK = """
            INSERT INTO task (task_id, title, description, status, date, hours, category_id, created_at, updated_at, version)
            VALUES (:taskId, :title, :description, :status, :date, :hours, :categoryId, :createdAt, :updatedAt, :version)
            """;
    private static final String UPDATE_TASK = """
            UPDATE task SET title = :title, description = :description, status = :status, date = :date, hours = :hours, category_id = :categoryId, updated_at = :updatedAt, version = :version
            WHERE task_id = :taskId AND version = :currentVersion
            """;
    private static final String DELETE_TASK = "DELETE FROM task WHERE task_id = :taskId";


    @Override
    public List<Task> findAll(int limit, int offset) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", limit);
        params.addValue("offset", offset);
        return template.query(FIND_ALL, params, TASK_MAPPER);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        SqlParameterSource params = new MapSqlParameterSource("taskId", id);
        try {
            return Optional.ofNullable(template.queryForObject(FIND_BY_ID, params, TASK_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Task> findByCategory(UUID categoryId) {
        SqlParameterSource params = new MapSqlParameterSource("categoryId", categoryId);
        return template.query(FIND_BY_CATEGORY_ID, params, TASK_MAPPER);
    }

    @Override
    public List<Task> findByStatus(String status) {
        SqlParameterSource params = new MapSqlParameterSource("status", status);
        return template.query(FIND_BY_STATUS, params, TASK_MAPPER);
    }

    @Override
    public int save(Task task) {
        MapSqlParameterSource params = createParameterSource(task);
        return template.update(SAVE_TASK, params);
    }

    @Override
    public int update(Task task, Long currentVersion) {
        MapSqlParameterSource params = createParameterSource(task);
        params.addValue("currentVersion", currentVersion);
        return template.update(UPDATE_TASK, params);
    }

    @Override
    public int delete(UUID taskId) {
        SqlParameterSource params =  new MapSqlParameterSource("taskId", taskId);
        return template.update(DELETE_TASK, params);
    }

    private MapSqlParameterSource createParameterSource(Task task) {
        return new MapSqlParameterSource()
                .addValue("taskId", task.getTaskID())
                .addValue("title", task.getTaskTitle())
                .addValue("description", task.getTaskDescription())
                .addValue("status", task.getTaskStatus().toString())
                .addValue("date", task.getTaskDate())
                .addValue("hours", task.getTaskHours())
                .addValue("categoryId", task.getTaskCategory())
                .addValue("createdAt", task.getCreatedAt())
                .addValue("updatedAt", task.getUpdatedAt())
                .addValue("version", task.getVersion());
    }
}
