package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с задачами ToDo с использованием JdbcTemplate.
 * Реализует методы поиска, сохранения, обновления и удаления задач в базе данных.
 */
@Repository
@RequiredArgsConstructor
public class ToDoRepositoryImpl implements ToDoRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Маппер для преобразования строки результата SQL запроса в объект ToDoItem.
     */
    private final RowMapper<ToDoItem> rowMapper = (rs, rowNum) -> new ToDoItem(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getBoolean("completed"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    /**
     * Получить все задачи с поддержкой пагинации.
     *
     * @param limit  Количество задач для выборки.
     * @param offset Смещение для выборки.
     * @return Список задач.
     */
    @Override
    public List<ToDoItem> findAll(int limit, int offset) {
        String sql = "SELECT * FROM todo_items ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, limit, offset);
    }

    /**
     * Найти задачу по ID.
     * @param id Идентификатор задачи.
     * @return Опциональная задача.
     */
    @Override
    public Optional<ToDoItem> findById(Long id) {
        String sql = "SELECT * FROM todo_items WHERE id = ?";
        List<ToDoItem> result = jdbcTemplate.query(sql, rowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /**
     * Сохранить новую задачу в базе данных.
     * @param toDoItem Задача для сохранения.
     */
    @Override
    public void save(ToDoItem toDoItem) {
        String sql = "INSERT INTO todo_items (title, description, completed, created_at) VALUES (?,?,?,?) RETURNING id";
        Long generatedId = jdbcTemplate.queryForObject(sql, Long.class,
                toDoItem.getTitle(), toDoItem.getDescription(), toDoItem.isCompleted(), toDoItem.getCreatedAt());
        toDoItem.setId(generatedId);
    }

    /**
     * Обновить существующую задачу.
     * @param toDoItem Задача с обновленными данными.
     */
    @Override
    public void update(ToDoItem toDoItem) {
        String sql = "UPDATE todo_items SET title = ?, description = ?, completed = ? WHERE id = ?";
        jdbcTemplate.update(sql, toDoItem.getTitle(), toDoItem.getDescription(), toDoItem.isCompleted(), toDoItem.getId());
    }

    /**
     * Удалить задачу по ID.
     * @param id Идентификатор задачи.
     */
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM todo_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
