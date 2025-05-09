package com.emobile.springtodo.controller;

import com.emobile.springtodo.SpringToDoApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest(classes = SpringToDoApplication.class)
@AutoConfigureMockMvc
public class ToDoControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.0")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("zenbook14");

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
        System.setProperty("spring.flyway.enabled", "true");
    }

    @Test
    void shouldGetToDoById() throws Exception {
        // Предполагаем, что миграция создала таблицу postgres
        String todoJson = "{\"title\":\"Test ToDo\",\"description\":\"Test Description\",\"completed\":false}";

        // Создаём ToDo
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test ToDo"));

        // Проверяем получение по ID
        mockMvc.perform(get("/api/todos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test ToDo"));
    }

    @Test
    void shouldReturn404ForNonExistentToDo() throws Exception {
        mockMvc.perform(get("/api/todos/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateToDoInput() throws Exception {
        String invalidTodoJson = "{\"title\":\"\",\"description\":\"Test Description\",\"completed\":false}";

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTodoJson))
                .andExpect(status().isBadRequest());
    }
}