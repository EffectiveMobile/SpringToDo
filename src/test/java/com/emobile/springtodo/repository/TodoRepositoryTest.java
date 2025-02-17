package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.repository.contract.TodoRepository;
import com.emobile.springtodo.repository.impl.TodoRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoRepositoryTest extends AbstractTestcontainers {

    private final TodoRepository todoRepository;

    public TodoRepositoryTest() {
        this.todoRepository = new TodoRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Test
    void testFindAll() {
        todoRepository.save(new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO));
        todoRepository.save(new Todo(null, "Task 2", "Description 2", TodoStatus.IN_PROGRESS));

        List<Todo> todos = todoRepository.findAll(10, 0);

        assertEquals(2, todos.size());
        assertEquals("Task 1", todos.get(0).getTitle());
        assertEquals("Task 2", todos.get(1).getTitle());
    }

    @Test
    void testFindById() {
        Todo savedTodo = todoRepository.save(new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO));

        Todo foundTodo = todoRepository.findById(savedTodo.getId());

        assertNotNull(foundTodo);
        assertEquals("Task 1", foundTodo.getTitle());
    }

    @Test
    void testSave() {
        Todo todo = new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO);

        Todo savedTodo = todoRepository.save(todo);
        
        assertNotNull(savedTodo.getId());
        assertEquals("Task 1", savedTodo.getTitle());
    }

    @Test
    void testUpdate() {
        Todo savedTodo = todoRepository.save(new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO));
        savedTodo.setTitle("Updated Task");
        savedTodo.setStatus(TodoStatus.COMPLETED);

        Todo updatedTodo = todoRepository.update(savedTodo);

        assertNotNull(updatedTodo);
        assertEquals("Updated Task", updatedTodo.getTitle());
        assertEquals(TodoStatus.COMPLETED, updatedTodo.getStatus());
    }

    @Test
    void testDelete() {
        Todo savedTodo = todoRepository.save(new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO));

        todoRepository.delete(savedTodo.getId());
        Todo deletedTodo = todoRepository.findById(savedTodo.getId());

        assertNull(deletedTodo);
    }
}