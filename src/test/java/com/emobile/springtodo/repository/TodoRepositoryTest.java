package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.repository.contract.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void testFindAllWithPagination() {
        todoRepository.save(new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO));
        todoRepository.save(new Todo(null, "Task 2", "Description 2", TodoStatus.IN_PROGRESS));
        todoRepository.save(new Todo(null, "Task 3", "Description 3", TodoStatus.COMPLETED));

        List<Todo> todos = todoRepository.findAll(2, 1);

        assertEquals(2, todos.size());
        assertEquals("Task 2", todos.get(0).getTitle());
        assertEquals("Task 3", todos.get(1).getTitle());
    }

    @Test
    void testFindById() {
        Todo savedTodo = todoRepository.save(new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO));

        Todo foundTodo = todoRepository.findById(savedTodo.getId()).orElse(null);

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
        Todo updatedTodo = todoRepository.save(savedTodo);

        assertNotNull(updatedTodo);
        assertEquals("Updated Task", updatedTodo.getTitle());
        assertEquals(TodoStatus.COMPLETED, updatedTodo.getStatus());
    }

    @Test
    void testDelete() {
        Todo savedTodo = todoRepository.save(new Todo(null, "Task 1", "Description 1", TodoStatus.TO_DO));

        todoRepository.deleteById(savedTodo.getId());

        Todo deletedTodo = todoRepository.findById(savedTodo.getId()).orElse(null);
        assertNull(deletedTodo);
    }
}