package com.emobile.springtodo;

import com.emobile.springtodo.api.request.CategoryCreationRequest;
import com.emobile.springtodo.api.request.CategoryUpdateRequest;
import com.emobile.springtodo.api.request.TaskCreationRequest;
import com.emobile.springtodo.api.request.TaskUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Sql(scripts = "/testsql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/testsql/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("taskdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0-alpine")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
    }

//  Integration Testing Category Controller

    @Test
    public void testCategoryControllerGetAll() throws Exception {
        mockMvc.perform(get("/category/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryTitle").value("Test Category1 Controller"))
                .andExpect(jsonPath("$[1].categoryTitle").value("Test Category2 Controller"));
    }

    @Test
    public void testCategoryControllerGetById() throws Exception {
        mockMvc.perform(get("/category/{categoryid}","2cb7304c-0116-464c-94e5-e4ea8b799461"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryTitle").value("Test Category1 Controller"))
                .andExpect(jsonPath("$.categoryColour").value("#CBDFBC"));
    }

    @Test
    public void testCategoryControllerGetByAccountId() throws Exception {
        mockMvc.perform(get("/category/account/{accountid}","5199a3a2-81ca-45d7-88e4-62310dcc09e1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryTitle").value("Test Category1 Controller"))
                .andExpect(jsonPath("$[1].categoryTitle").value("Test Category2 Controller"));
    }

    @Test
    public void testCategoryControllerCreateCategory() throws Exception {
        CategoryCreationRequest request = new CategoryCreationRequest(
                "Test Category Service",
                "#CBDFBC",
                "5199a3a2-81ca-45d7-88e4-62310dcc09e1"
        );

        mockMvc.perform(post("/category/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    public void testCategoryControllerUpdateCategory() throws Exception {
        CategoryUpdateRequest request = new CategoryUpdateRequest(
                "Updated Test Category Service",
                "#CBDFBF"
        );

        mockMvc.perform(put("/category/{categoryid}", "2cb7304c-0116-464c-94e5-e4ea8b799461")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/category/{categoryid}","2cb7304c-0116-464c-94e5-e4ea8b799461"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryTitle").value("Updated Test Category Service"))
                .andExpect(jsonPath("$.categoryColour").value("#CBDFBF"));
    }

    @Test
    public void testCategoryControllerDeleteCategory() throws Exception {
        mockMvc.perform(delete("/category/{categoryid}","2cb7304c-0116-464c-94e5-e4ea8b799461"))
                .andExpect(status().isNoContent());
    }

//  Integration Testing Task Controller

    @Test
    public void testTaskControllerGetAll() throws Exception {
        mockMvc.perform(get("/task/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].taskTitle").value("Test Task1 Controller"))
                .andExpect(jsonPath("$[1].taskTitle").value("Test Task2 Controller"))
                .andExpect(jsonPath("$[0].taskDescription").value("Test Task1 Description"))
                .andExpect(jsonPath("$[1].taskDescription").value("Test Task2 Description"));
    }

    @Test
    public void testTaskControllerGetByTaskId() throws Exception {
        mockMvc.perform(get("/task/{taskid}", "09f535f1-d205-4cf0-b5fe-b31be8605b91"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskTitle").value("Test Task1 Controller"))
                .andExpect(jsonPath("$.taskDescription").value("Test Task1 Description"));
    }

    @Test
    public void testTaskControllerGetByCategory() throws Exception {
        mockMvc.perform(get("/task/category/{categoryid}", "2cb7304c-0116-464c-94e5-e4ea8b799461"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].taskTitle").value("Test Task1 Controller"))
                .andExpect(jsonPath("$[1].taskTitle").value("Test Task2 Controller"))
                .andExpect(jsonPath("$[0].taskDescription").value("Test Task1 Description"))
                .andExpect(jsonPath("$[1].taskDescription").value("Test Task2 Description"));

    }

    @Test
    public void testTaskControllerGetByStatus() throws Exception {
        mockMvc.perform(get("/task/status/{status}", "TODO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].taskTitle").value("Test Task1 Controller"))
                .andExpect(jsonPath("$[1].taskTitle").value("Test Task2 Controller"))
                .andExpect(jsonPath("$[0].taskStatus").value("TODO"))
                .andExpect(jsonPath("$[1].taskStatus").value("TODO"));
    }

    @Test
    public void testTaskControllerCreateTask() throws Exception {
        TaskCreationRequest request = new TaskCreationRequest(
                "Test Task Service From Request",
                "Test Task Description",
                "TODO",
                Date.valueOf("2024-11-13"),
                4.5F,
                "3dc7304c-0226-464c-94e5-e4ea8b799462");

        mockMvc.perform(post("/task/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testTaskControllerUpdateTask() throws Exception {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Updated Task Title From Request",
                "Updated Task Description",
                "DELAYED",
                Date.valueOf("2024-11-16"),
                10F,
                "3dc7304c-0226-464c-94e5-e4ea8b799462"
        );

        mockMvc.perform(put("/task/{taskid}", "09f535f1-d205-4cf0-b5fe-b31be8605b91")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/task/{taskid}", "09f535f1-d205-4cf0-b5fe-b31be8605b91"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskTitle").value("Updated Task Title From Request"))
                .andExpect(jsonPath("$.taskDescription").value("Updated Task Description"))
                .andExpect(jsonPath("$.taskStatus").value("DELAYED"));
    }

    @Test
    public void testTaskControllerDeleteTask() throws Exception {
        mockMvc.perform(delete("/task/{taskid}", "09f535f1-d205-4cf0-b5fe-b31be8605b91"))
                .andExpect(status().isNoContent());
    }
}
