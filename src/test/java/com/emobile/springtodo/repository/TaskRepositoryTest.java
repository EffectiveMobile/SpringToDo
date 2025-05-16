package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Status;
import com.emobile.springtodo.model.TaskEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@SpringBootTest
@DisplayName("Тесты репозитория задач")
class TaskRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @Autowired
    private TaskRepository taskRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @DisplayName("Поиск всех задач с пагинацией")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllTaskTest() {
        // Given
        int limit = 2;
        int offset = 1;

        // When
        List<TaskEntity> tasks = taskRepository.findAllTask(limit, offset);

        // Then
        assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("Поиск задач по названию")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findTaskByTitleTest() {
        // Given
        int limit = 10;
        int offset = 0;

        // When
        List<TaskEntity> tasks = taskRepository.findTaskByTitle("Task", limit, offset);

        // Then
        assertEquals(3, tasks.size());
        assertEquals("Task 1", tasks.getFirst().getTitle());
    }

    @Test
    @DisplayName("Поиск задач по статусу")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findTaskByStatusTest() {
        // Given
        String status = Status.PENDING.name();
        int limit = 10;
        int offset = 0;

        // When
        List<TaskEntity> tasks = taskRepository.findTaskByStatus(status, limit, offset);

        // Then
        assertEquals(2, tasks.size());
        assertEquals(Status.PENDING, tasks.get(0).getStatus());
    }

    @Test
    @DisplayName("Удаление задачи по названию")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteTaskByTitleTest() {
        // Given
        String titleToDelete = "Task 1";

        // When
        taskRepository.deleteTaskByTitle(titleToDelete);

        // Then
        assertFalse(taskRepository.existTaskByTitle(titleToDelete));
    }

    @Test
    @DisplayName("Создание новой задачи")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createTaskTest() {
        // Given
        TaskEntity newTask = new TaskEntity();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setStatus(Status.PENDING);

        // When
        taskRepository.createTask(newTask);

        // Then
        assertTrue(taskRepository.existTaskByTitle(newTask.getTitle()));
    }

    @Test
    @DisplayName("Изменение статуса задачи")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editStatusTest() {
        // Given
        String title = "Task 1";
        Status newStatus = Status.COMPLETED;

        // When
        taskRepository.editStatus(title, newStatus);

        // Then
        List<TaskEntity> tasks = taskRepository.findTaskByTitle(title, 1, 0);
        assertEquals(Status.COMPLETED, tasks.get(0).getStatus());
    }

    @Test
    @DisplayName("Проверка существования задачи (положительный случай)")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void existTaskByTitleTestTrue() {
        // Given
        String existingTitle = "Task 1";

        // When
        boolean exists = taskRepository.existTaskByTitle(existingTitle);

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Проверка существования задачи (отрицательный случай)")
    @Sql(scripts = "/sql/insert_test_tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/clean_tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void existTaskByTitleTestFalse() {
        // Given
        String nonExistingTitle = "aaaaaaaaaaaaaaaaaaaaa";

        // When
        boolean exists = taskRepository.existTaskByTitle(nonExistingTitle);

        // Then
        assertFalse(exists);
    }
}