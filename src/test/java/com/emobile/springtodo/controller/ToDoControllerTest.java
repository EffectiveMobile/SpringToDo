package com.emobile.springtodo.controller;

import com.emobile.springtodo.SpringToDoApplication;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest(classes = SpringToDoApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ToDoControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.8")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("zenbook14");

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
    }

    @BeforeEach
    void setUp() {
        Flyway flyway = Flyway.configure()
                .dataSource(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword())
                .locations("classpath:db/migration")
                .cleanDisabled(false)
                .load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @DisplayName("Should create and retrieve ToDo by ID")
    @Sql("/insert-todo.sql") // Подготовка данных
    void shouldCreateAndGetToDoById() throws Exception {
        String todoJson = "{\"title\":\"Test ToDo\",\"description\":\"Test Description\",\"completed\":false}";
        String expectedJson = "{\"id\":1,\"title\":\"Test ToDo\",\"description\":\"Test Description\",\"completed\":false}";

        // Создаём ToDo
        MvcResult createResult = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, createResult.getResponse().getContentAsString(), false);

        // Проверяем получение по ID
        MvcResult getResult = mockMvc.perform(get("/api/todos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, getResult.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Should return 404 for non-existent ToDo")
    void shouldReturn404ForNonExistentToDo() throws Exception {
        String expectedErrorJson = "{\"error\":\"ToDo not found with id: 999\"}";

        MvcResult result = mockMvc.perform(get("/api/todos/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        JSONAssert.assertEquals(expectedErrorJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    void shouldValidateToDoInput() throws Exception {
        String invalidToDoJson = "{\"title\":\"\",\"description\":\"Test Description\",\"completed\":false}";
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidToDoJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"title\":\"Title is mandatory\"}"));
    }

    @Test
    @DisplayName("Should return paginated list of ToDos")
    @Sql("/insert-multiple-todos.sql") // Вставка нескольких записей
    void shouldReturnPaginatedToDos() throws Exception {
        String expectedJson = """
                {
                    "content": [
                        {"id":1,"title":"ToDo 1","description":"Desc 1","completed":false},
                        {"id":2,"title":"ToDo 2","description":"Desc 2","completed":true}
                    ],
                    "totalElements": 2,
                    "totalPages": 1,
                    "page": 0,
                    "size": 10
                }
                """;

        MvcResult result = mockMvc.perform(get("/api/todos?limit=10&offset=0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Should cache ToDo retrieval")
    void shouldCacheToDoRetrieval() throws Exception {
        String todoJson = "{\"title\":\"Cached ToDo\",\"description\":\"Cached Description\",\"completed\":false}";
        String expectedJson = "{\"id\":1,\"title\":\"Cached ToDo\",\"description\":\"Cached Description\",\"completed\":false}";

        // Создаём ToDo
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isOk());

        // Первый вызов - должен попасть в базу
        MvcResult firstCall = mockMvc.perform(get("/api/todos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, firstCall.getResponse().getContentAsString(), false);

        // Второй вызов - должен взять из кеша
        MvcResult secondCall = mockMvc.perform(get("/api/todos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, secondCall.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Should return Swagger API documentation")
    void shouldReturnSwaggerDocumentation() throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"openapi\":\"3.0.1\"}", response, false);
    }
}
