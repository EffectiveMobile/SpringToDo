package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ToDoRepositoryImpl implements ToDoRepository {
    private final JdbcTemplate jdbcTemplate;


    private final RowMapper<ToDoItem> rowMapper = (rs, rowNum) -> new ToDoItem(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getBoolean("completed"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    @Override
    public List<ToDoItem> findAll(int limit, int offset) {
        String sql = "SELECT * FROM todo_items ORDER BY created_at DESC LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql, rowMapper, limit, offset);
    }

    @Override
    public Optional<ToDoItem> findById(Long id) {
        String sql = "SELECT * FROM todo_items WHERE id = ?";
        List<ToDoItem> result = jdbcTemplate.query(sql, rowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    public void save(ToDoItem toDoItem) {
        String sql = "INSERT INTO todo_items (title, description, completed, created_at) VALUES (?,?,?,?) RETURNING id";
        Long generatedId = jdbcTemplate.queryForObject(sql, Long.class,
                toDoItem.getTitle(), toDoItem.getDescription(), toDoItem.isCompleted(), toDoItem.getCreatedAt());
        toDoItem.setId(generatedId);

    }

    @Override
    public void update(ToDoItem toDoItem) {
        String sql = "UPDATE todo_items SET title = ?, description = ?, completed = ? WHERE id = ?";
        jdbcTemplate.update(sql, toDoItem.getTitle(), toDoItem.getDescription(), toDoItem.isCompleted(), toDoItem.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM todo_items WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }
}
