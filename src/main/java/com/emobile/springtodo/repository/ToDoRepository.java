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
        if (entity.getId() == null) {
            String sql = "INSERT INTO postgres(title, description, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class, entity.getTitle(), entity.getDescription(), entity.isCompleted(), LocalDateTime.now(), LocalDateTime.now());
            entity.setId(id);
        }else{
            String sql = "UPDATE postgres SET title = ?, description = ?, completed = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, entity.getTitle(), entity.getDescription(), entity.isCompleted(), LocalDateTime.now(), entity.getId());
        }
        return entity;
    }

    public Optional<ToDoEntity> findById(Long id){
        String sql = "SELECT * FROM postgres WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public List<ToDoEntity> findAll(int limit, int offset){
        String sql = "SELECT * FROM postgres ORDER BY LIMIT ?, OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, limit, offset);
    }

    public void deleteByID(Long id){
        String sql = "DELETE FROM postgres WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
