package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.CreateTodo;
import com.emobile.springtodo.dto.UpdateTodo;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.model.TodoStatus;
import com.emobile.springtodo.service.contract.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    void testGetAllTodos() throws Exception {
        List<Todo> mockTodos = Arrays.asList(
                new Todo(1L, "Task 1", "Description 1", TodoStatus.TO_DO),
                new Todo(2L, "Task 2", "Description 2", TodoStatus.IN_PROGRESS)
        );
        when(todoService.getAllTodos(10, 0)).thenReturn(mockTodos);

        mockMvc.perform(get("/api/todos")
                        .param("limit", "10")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(todoService, times(1)).getAllTodos(10, 0);
    }

    @Test
    void testGetTodoById() throws Exception {
        Todo mockTodo = new Todo(1L, "Task 1", "Description 1", TodoStatus.TO_DO);
        when(todoService.getTodoById(1L)).thenReturn(mockTodo);

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"));

        verify(todoService, times(1)).getTodoById(1L);
    }

    @Test
    void testSaveTodo() throws Exception {
        CreateTodo createTodo = new CreateTodo("New Task", "New Description", TodoStatus.TO_DO);
        Todo savedTodo = new Todo(1L, "New Task", "New Description", TodoStatus.TO_DO);
        when(todoService.saveTodo(any(CreateTodo.class))).thenReturn(savedTodo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTodo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));

        verify(todoService, times(1)).saveTodo(any(CreateTodo.class));
    }

    @Test
    void testUpdateTodo() throws Exception {
        UpdateTodo updateTodo = new UpdateTodo("Updated Task", "Updated Description", TodoStatus.COMPLETED);
        Todo updatedTodo = new Todo(1L, "Updated Task", "Updated Description", TodoStatus.COMPLETED);
        when(todoService.updateTodo(eq(1L), any(UpdateTodo.class))).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTodo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(todoService, times(1)).updateTodo(eq(1L), any(UpdateTodo.class));
    }

    @Test
    void testDeleteTodo() throws Exception {
        doNothing().when(todoService).deleteTodo(1L);

        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).deleteTodo(1L);
    }
}
