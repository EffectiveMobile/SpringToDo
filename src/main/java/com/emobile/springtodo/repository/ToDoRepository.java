package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDoEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ToDoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ToDoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<ToDoEntity> rowMapper = (rs, rowNum) -> {
        ToDoEntity entity = new ToDoEntity();
        entity.setId(rs.getLong("id"));
        entity.setTitle(rs.getString("title"));
        entity.setDescription(rs.getString("description"));
        entity.setCompleted(rs.getBoolean("completed"));
        entity.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        entity.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return entity;
    };

public ToDoEntity save(ToDoEntity entity) {
    System.out.println("Saving ToDoEntity: " + entity);
    try {
        if (entity.getId() == null) {
            String sql = "INSERT INTO todos(title, description, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class, entity.getTitle(), entity.getDescription(), entity.isCompleted(), LocalDateTime.now(), LocalDateTime.now());
            entity.setId(id);
            System.out.println("Inserted ToDoEntity with id: " + id);
        } else {
            String sql = "UPDATE todos SET title = ?, description = ?, completed = ?, updated_at = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, entity.getTitle(), entity.getDescription(), entity.isCompleted(), LocalDateTime.now(), entity.getId());
            System.out.println("Updated ToDoEntity with id: " + entity.getId() + ", rows affected: " + rowsAffected);
        }
        System.out.println("Saved ToDoEntity: " + entity);
    } catch (Exception e) {
        System.err.println("Error saving ToDoEntity: " + e.getMessage());
        throw new RuntimeException("Failed to save ToDoEntity", e);
    }
    return entity;
}

    public Optional<ToDoEntity> findById(Long id){
        String sql = "SELECT * FROM todos WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

public List<ToDoEntity> findAll(int limit, int offset) {
    String sql = "SELECT * FROM todos ORDER BY id LIMIT ? OFFSET ?";
    System.out.println("Executing findAll with limit: " + limit + ", offset: " + offset);
    List<ToDoEntity> result = jdbcTemplate.query(sql, rowMapper, limit, offset);
    System.out.println("Found " + result.size() + " records");
    return result;
}

    public void deleteByID(Long id){
        String sql = "DELETE FROM todos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
