package com.emobile.springtodo.repository.impl;

import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.repository.contract.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        List<Todo> results = jdbcTemplate.query(sql, new TodoRowMapper(), id);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public Todo save(Todo todo) {
        String sql = "INSERT INTO todos (title, description, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, todo.getTitle());
            ps.setString(2, todo.getDescription());
            ps.setString(3, todo.getStatus().name());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        todo.setId(generatedId);
        return todo;
    }

    @Override
    public Todo update(Todo todo) {
        String sql = "UPDATE todos SET title = ?, description = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, todo.getTitle(), todo.getDescription(), todo.getStatus().name(), todo.getId());
        return findById(todo.getId());
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
