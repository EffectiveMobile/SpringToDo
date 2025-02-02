package com.emobile.springtodo.repositories;

import com.emobile.springtodo.entities.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JdbcRepository<Long, Task> {

    Optional<List<Task>> findAllByTitle(String title, Integer offset, Integer limit);

}
