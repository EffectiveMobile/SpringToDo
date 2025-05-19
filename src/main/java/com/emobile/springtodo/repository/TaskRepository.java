package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Status;
import com.emobile.springtodo.model.TaskEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;


/**
 * Repository class for managing task entities in the database.
 */
@Repository
public class TaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper implementation for TaskEntity
     */
    private final RowMapper<TaskEntity> taskRowMapper = (resultSet, rowNum) -> {
        TaskEntity task = new TaskEntity();
        task.setId(resultSet.getInt("id"));
        task.setTitle(resultSet.getString("title"));
        task.setDescription(resultSet.getString("description"));
        task.setStatus(Status.valueOf(resultSet.getString("status")));
        return task;
    };


    /**
     * Return all tasks with pagination support.
     *
     * @param limit  maximum number of tasks to return
     * @param offset number of tasks to skip
     * @return list of tasks ordered by ID
     */
    public List<TaskEntity> findAllTask(Integer limit, Integer offset) {
        String sql = "SELECT * FROM tasks ORDER BY id LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, taskRowMapper, limit, offset);
    }

    /**
     * Finds tasks containing the specified title string.
     *
     * @param title  title pattern to search for (uses SQL LIKE pattern)
     * @param limit  maximum number of tasks to return
     * @param offset number of tasks to skip
     * @return list of matching tasks
     */
    public List<TaskEntity> findTaskByTitle(String title, Integer limit, Integer offset) {
        String sql = "SELECT * FROM tasks WHERE title LIKE ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, taskRowMapper, "%" + title + "%", limit, offset);
    }

    /**
     * Finds tasks with the specified status.
     *
     * @param status status to filter by
     * @param limit  maximum number of tasks to return
     * @param offset number of tasks to skip
     * @return list of tasks with matching status
     */
    public List<TaskEntity> findTaskByStatus(String status, Integer limit, Integer offset) {
        String sql = "SELECT * FROM tasks WHERE status = ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, taskRowMapper, status, limit, offset);
    }

    /**
     * Deletes a task by its exact title match.
     *
     * @param title exact title of the task to delete
     */
    public void deleteTaskByTitle(String title) {
        String sql = "DELETE FROM tasks WHERE title = ?";
        jdbcTemplate.update(sql, title);
    }

    /**
     * Creates a new task in the database.
     *
     * @param task the task entity to create
     */
    public void createTask(TaskEntity task) {
        String sql = "INSERT INTO tasks (title, description, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getStatus().name());
    }

    /**
     * Updates the status of a task identified by its title.
     *
     * @param title  exact title of the task to update
     * @param status new status to set
     */
    public void editStatus(String title, Status status) {
        String sql = "UPDATE tasks SET status = ? WHERE title = ?";
        jdbcTemplate.update(sql, status.name(), title);
    }


    /**
     * Checks if a task with the specified title exists.
     *
     * @param title exact title to check
     * @return true if a task with this title exists, false otherwise
     */
    public boolean existTaskByTitle(String title) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE title = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, title);
        return count > 0;
    }
}
