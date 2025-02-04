package com.emobile.springtodo.repository.impl;

import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.repository.contract.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TodoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Todo> findAll(int limit, int offset) {
        String sql = "SELECT * FROM todos ORDER BY id LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new TodoRowMapper(), limit, offset);
    }

    @Override
    public Todo findById(Long id) {
        String sql = "SELECT * FROM todos WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new TodoRowMapper(), id);
    }

    @Override
    public void save(Todo todo) {
        String sql = "INSERT INTO todos (title, description, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, todo.getTitle(), todo.getDescription(), todo.getStatus().name());
    }

    @Override
    public void update(Todo todo) {
        String sql = "UPDATE todos SET title = ?, description = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, todo.getTitle(), todo.getDescription(), todo.getStatus().name(), todo.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM todos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class TodoRowMapper implements RowMapper<Todo> {
        @Override
        public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
            Todo todo = new Todo();
            todo.setId(rs.getLong("id"));
            todo.setTitle(rs.getString("title"));
            todo.setDescription(rs.getString("description"));
            todo.setStatus(TodoStatus.valueOf(rs.getString("status")));
            return todo;
        }
    }
}
