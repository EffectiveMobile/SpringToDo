package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.CreateTodo;
import com.emobile.springtodo.dto.UpdateTodo;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.repository.contract.TodoRepository;
import com.emobile.springtodo.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTodos() {
        List<Todo> mockTodos = Arrays.asList(
                new Todo(1L, "Task 1", "Description 1", TodoStatus.TO_DO),
                new Todo(2L, "Task 2", "Description 2", TodoStatus.IN_PROGRESS)
        );
        when(todoRepository.findAll(10, 0)).thenReturn(mockTodos);

        List<Todo> todos = todoService.getAllTodos(10, 0);

        assertEquals(2, todos.size());
        assertEquals("Task 1", todos.get(0).getTitle());
        verify(todoRepository, times(1)).findAll(10, 0);
    }

    @Test
    void testGetTodoById() {
        Todo mockTodo = new Todo(1L, "Task 1", "Description 1", TodoStatus.TO_DO);
        when(todoRepository.findById(1L)).thenReturn(mockTodo);

        Todo todo = todoService.getTodoById(1L);

        assertNotNull(todo);
        assertEquals("Task 1", todo.getTitle());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTodoByIdThrowsException() {
        when(todoRepository.findById(1L)).thenReturn(null);

        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(1L));
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveTodo() {
        CreateTodo createTodo = new CreateTodo("New Task", "New Description", TodoStatus.TO_DO);
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            todo.setId(1L);
            return todo;
        });

        Todo result = todoService.saveTodo(createTodo);

        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void testUpdateTodo() {
        UpdateTodo updateTodo = new UpdateTodo("Updated Task", "Updated Description", TodoStatus.COMPLETED);
        Todo existingTodo = new Todo(1L, "Task 1", "Description 1", TodoStatus.TO_DO);
        when(todoRepository.findById(1L)).thenReturn(existingTodo);
        when(todoRepository.update(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Todo updatedTodo = todoService.updateTodo(1L, updateTodo);

        assertNotNull(updatedTodo);
        assertEquals("Updated Task", updatedTodo.getTitle());
        assertEquals(TodoStatus.COMPLETED, updatedTodo.getStatus());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).update(any(Todo.class));
    }

    @Test
    void testDeleteTodo() {
        Todo existingTodo = new Todo(1L, "Task 1", "Description 1", TodoStatus.TO_DO);
        when(todoRepository.findById(1L)).thenReturn(existingTodo);
        doNothing().when(todoRepository).delete(1L);

        todoService.deleteTodo(1L);

        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).delete(1L);
    }
}
