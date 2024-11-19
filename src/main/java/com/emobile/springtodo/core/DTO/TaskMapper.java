package com.emobile.springtodo.core.DTO;

import com.emobile.springtodo.core.entity.Status;
import com.emobile.springtodo.core.entity.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TaskMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = Task.builder()
                .taskID(UUID.fromString(rs.getString("task_id")))
                .taskTitle(rs.getString("title"))
                .taskDescription(rs.getString("description"))
                .taskStatus(Status.valueOf(rs.getString("status")))
                .taskDate(rs.getTimestamp("date"))
                .taskHours(rs.getFloat("hours"))
                .taskCategory(UUID.fromString(rs.getString("category_id")))
                .build();

        task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        task.setVersion(rs.getLong("version"));
        return task;
    }
}
