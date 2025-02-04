package com.emobile.springtodo.repositories;

import com.emobile.springtodo.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.emobile.springtodo.other.ConstantsClass.*;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task save(Task entity) {
        String sql = "INSERT INTO tasks_entity (title, description, status, dateCalendar) " +
                "VALUES (?, ?, ?, ?)";
        commonUpdate(sql, entity);
        return entity;
    }

    @Override
    public Task update(Task newEntity) {
        String sql = "UPDATE tasks_entity SET title = ?, description = ?, status = ?, dateCalendar = ? WHERE id = ? ";
        commonUpdate(sql, newEntity);
        return newEntity;
    }

    @Override
    public Task findById(Long id) {
        String sql = "SELECT id, title, description, status, dateCalendar " +
                "FROM tasks_entity WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    @Override
    public Optional<List<Task>> findAllByTitle(String title, Integer offset, Integer limit) {
        String sql = "SELECT id, title, description, status, dateCalendar " +
                "FROM tasks_entity WHERE title = ? ORDER BY id LIMIT ? OFFSET ?";
        return Optional.of(jdbcTemplate.query(sql, rowMapper(), title, offset, limit));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tasks_entity WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Task> rowMapper() {
        return (rs, rowNum) -> {
            Task task = new Task();
            task.setId(rs.getLong(ID_FIELD));
            task.setTitle(rs.getString(TITLE_FIELD));
            task.setDescription(rs.getString(DESCRIPTION_FIELD));
            task.setStatus(rs.getBoolean(STATUS_FIELD));
            task.setDateCalendar(rs.getTimestamp(DATA_CALENDAR_FIELD).toLocalDateTime()); // ✅ Timestamp → LocalDateTime
            return task;
        };
    }

    /**
     * Метод, сохраняющий, либо обновляющий запись в таблице. В объекте поле id null, если сохранение, либо обновление не удалось
     * (т.к update возвращает кол-во измененных строк)
     *
     * @param sql    SQL - запрос на обновление сущности, либо на сохранение
     * @param entity Объект для сохрнения, либо обновления в БД
     */
    private void commonUpdate(String sql, Task entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatement(sql, entity), keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            entity.setId(key.longValue());
        }
    }

    /**
     * Метод, собирающий PreparedStatement через интерфейс PreparedStatementCreator для отправки запроса с параметрами к БД
     *
     * @param sql    SQL - запрос на обновление сущности, либо на сохранение
     * @param entity Объект для сохрнения, либо обновления в БД
     * @return интерфейс {@link PreparedStatementCreator}
     */
    private PreparedStatementCreator preparedStatement(String sql, Task entity) {

        return connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{ID_FIELD});
            int counter = 1;
            if (entity.getTitle() != null) {
                ps.setString(counter, entity.getTitle());
                counter++;
            }
            if (entity.getDescription() != null) {
                ps.setString(counter, entity.getDescription());
                counter++;
            }
            if (entity.getStatus() != null) {
                ps.setBoolean(counter, entity.getStatus());
                counter++;
            }
            if (entity.getDateCalendar() != null) {
                ps.setTimestamp(counter, Timestamp.valueOf(entity.getDateCalendar()));
                counter++;
            }
            if (entity.getId() != null) {
                ps.setLong(counter, entity.getId());
            }
            return ps;
        };
    }
}
