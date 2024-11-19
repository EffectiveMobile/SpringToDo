package com.emobile.springtodo.core.repository;

import com.emobile.springtodo.core.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepo {

    List<Task> findAll(int limit, int offset);
    Optional<Task> findById(UUID id);
    List<Task> findByCategory(UUID category);
    List<Task> findByStatus(String status);
    int save(Task task);
    int update(Task task, Long currentVersion);
    int delete(UUID taskId);

}
