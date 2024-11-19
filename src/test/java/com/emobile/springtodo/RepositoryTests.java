package com.emobile.springtodo;

import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.entity.Status;
import com.emobile.springtodo.core.entity.Task;
import com.emobile.springtodo.core.repository.CategoryRepoImpl;
import com.emobile.springtodo.core.repository.TaskRepoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@Sql(scripts = "/testsql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RepositoryTests {

    @Autowired
    CategoryRepoImpl categoryRepo;

    @Autowired
    TaskRepoImpl taskRepo;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("taskdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

//  Testing Category Repository

    @Test
    public void testInsertingCategory(){
        Category expectedCategory = createCategory();

        categoryRepo.save(expectedCategory);

        Optional<Category> actualCategory = categoryRepo.findById(UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));

        Assertions.assertTrue(actualCategory.isPresent(), "Category should be present in the repository");
        Assertions.assertEquals(actualCategory.get().getCategoryTitle(), expectedCategory.getCategoryTitle());
        Assertions.assertEquals(actualCategory.get().getCategoryColour(), expectedCategory.getCategoryColour());
        Assertions.assertEquals(actualCategory.get().getAccountID(), expectedCategory.getAccountID());
        Assertions.assertEquals(
                actualCategory.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS)
        );

        Assertions.assertEquals(
                actualCategory.get().getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS)
        );
        Assertions.assertEquals(actualCategory.get().getVersion(), expectedCategory.getVersion());
    }


    @Test
    public void testUpdatingCategory(){
        Category expectedCategory = createCategory();

        categoryRepo.save(expectedCategory);

        LocalDateTime expectedUpdatedDate = LocalDateTime.now();
        Long currentVersion = expectedCategory.getVersion();

        expectedCategory.setCategoryColour("#F7F4D3");
        expectedCategory.setCategoryTitle("Updated Test Category");
        expectedCategory.setUpdatedAt(expectedUpdatedDate);
        expectedCategory.setVersion(expectedCategory.getVersion() + 1);

        categoryRepo.update(expectedCategory, currentVersion);

        Optional<Category> actualCategory = categoryRepo.findById(expectedCategory.getCategoryID());
        Assertions.assertTrue(actualCategory.isPresent(), "Category should be present in the repository");
        Assertions.assertEquals(actualCategory.get().getCategoryTitle(), expectedCategory.getCategoryTitle());
        Assertions.assertEquals(actualCategory.get().getCategoryColour(), expectedCategory.getCategoryColour());
        Assertions.assertEquals(
                actualCategory.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS)
        );

        Assertions.assertEquals(
                actualCategory.get().getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS)
        );
        Assertions.assertEquals(actualCategory.get().getVersion(), expectedCategory.getVersion());
    }

    @Test
    public void testDeletingCategory(){
        Category expectedCategory = createCategory();

        categoryRepo.save(expectedCategory);

        Optional<Category> actualCategory = categoryRepo.findById(expectedCategory.getCategoryID());
        Assertions.assertTrue(actualCategory.isPresent(), "Category should be present in the repository");

        categoryRepo.delete(expectedCategory.getCategoryID());
        Optional<Category> actualCategory2 = categoryRepo.findById(expectedCategory.getCategoryID());
        Assertions.assertFalse(actualCategory2.isPresent(), "Category should not be present in the repository");
    }

    @Test
    public void testFindingCategoryByAccountID(){
        Category expectedCategory = createCategory();

        categoryRepo.save(expectedCategory);

        categoryRepo.findByAccountId(expectedCategory.getAccountID());
        Optional<Category> actualCategory = categoryRepo.findById(expectedCategory.getCategoryID());
        Assertions.assertTrue(actualCategory.isPresent(), "Category should be present in the repository");
    }

    @Test
    public void testFindingAllCategories(){
        Category expectedCategory = createCategory();

        Category expectedCategory2 = createCategory();
        expectedCategory2.setCategoryID(UUID.fromString("0f53bd22-893b-4277-b90f-f5a9b71c6a82"));
        expectedCategory2.setCategoryTitle("Test Category 2");

        categoryRepo.save(expectedCategory);
        categoryRepo.save(expectedCategory2);

        List<Category> categoryList = categoryRepo.findAll();

        categoryList.forEach(System.out::println);

        Assertions.assertEquals(2, categoryList.size(), "There should be exactly 2 categories in the repository");
        Assertions.assertTrue(categoryList.stream()
                        .anyMatch(category -> category.getCategoryID().equals(expectedCategory.getCategoryID()) &&
                                category.getCategoryTitle().equals(expectedCategory.getCategoryTitle()) &&
                                category.getCategoryColour().equals(expectedCategory.getCategoryColour()) &&
                                category.getAccountID().equals(expectedCategory.getAccountID())),
                "Category should be present in the repository");

        Assertions.assertTrue(categoryList.stream()
                        .anyMatch(category -> category.getCategoryID().equals(expectedCategory2.getCategoryID()) &&
                                category.getCategoryTitle().equals(expectedCategory2.getCategoryTitle()) &&
                                category.getCategoryColour().equals(expectedCategory2.getCategoryColour()) &&
                                category.getAccountID().equals(expectedCategory2.getAccountID())),
                "Category should be present in the repository");
    }

//  Testing Task Repository

    @Test
    public void testInsertingTask(){
        Task expectedTask = createTask();

        taskRepo.save(expectedTask);

        Optional<Task> actualTask = taskRepo.findById(expectedTask.getTaskID());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Assertions.assertTrue(actualTask.isPresent(), "Task should be present in the repository");
        Assertions.assertEquals(actualTask.get().getTaskTitle(), expectedTask.getTaskTitle());
        Assertions.assertEquals(actualTask.get().getTaskDescription(), expectedTask.getTaskDescription());
        Assertions.assertEquals(actualTask.get().getTaskID(), expectedTask.getTaskID());
        Assertions.assertEquals(actualTask.get().getTaskStatus(), expectedTask.getTaskStatus());
        Assertions.assertEquals(actualTask.get().getTaskHours(), expectedTask.getTaskHours());
        Assertions.assertEquals(sdf.format(actualTask.get().getTaskDate()), sdf.format(expectedTask.getTaskDate()));
        Assertions.assertEquals(actualTask.get().getTaskCategory(), expectedTask.getTaskCategory());
        Assertions.assertEquals(
                actualTask.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS)
        );

        Assertions.assertEquals(
                actualTask.get().getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS)
        );
        Assertions.assertEquals(actualTask.get().getVersion(), expectedTask.getVersion());
    }

    @Test
    public void testUpdatingTask(){
        Task expectedTask = createTask();

        taskRepo.save(expectedTask);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        LocalDateTime expectedUpdatedDate = LocalDateTime.now();
        Long currentVersion = expectedTask.getVersion();

        expectedTask.setTaskStatus(Status.COMPLETE);
        expectedTask.setTaskTitle("Updated Test Task");
        expectedTask.setTaskDescription("Updated Test Task Description");
        expectedTask.setTaskDate(Date.valueOf("2024-11-14"));
        expectedTask.setTaskHours(1.5F);
        expectedTask.setUpdatedAt(expectedUpdatedDate);
        expectedTask.setVersion(expectedTask.getVersion() + 1);

        taskRepo.update(expectedTask, currentVersion);

        Optional<Task> actualTask = taskRepo.findById(expectedTask.getTaskID());

        Assertions.assertTrue(actualTask.isPresent(), "Task should be present in the repository");
        Assertions.assertEquals(actualTask.get().getTaskTitle(), expectedTask.getTaskTitle());
        Assertions.assertEquals(actualTask.get().getTaskDescription(), expectedTask.getTaskDescription());
        Assertions.assertEquals(sdf.format(actualTask.get().getTaskDate()), sdf.format(expectedTask.getTaskDate()));
        Assertions.assertEquals(actualTask.get().getTaskHours(), expectedTask.getTaskHours());
        Assertions.assertEquals(actualTask.get().getTaskStatus(), expectedTask.getTaskStatus());
        Assertions.assertEquals(
                actualTask.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS)
        );

        Assertions.assertEquals(
                actualTask.get().getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
                expectedTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS)
        );
        Assertions.assertEquals(actualTask.get().getVersion(), expectedTask.getVersion());
    }

    @Test
    public void testDeletingTask(){
        Task expectedTask = createTask();

        taskRepo.save(expectedTask);

        Optional<Task> actualTask = taskRepo.findById(expectedTask.getTaskID());
        Assertions.assertTrue(actualTask.isPresent(), "Task should be present in the repository");

        taskRepo.delete(expectedTask.getTaskID());

        Optional<Task> actualTask2 = taskRepo.findById(expectedTask.getTaskID());
        Assertions.assertFalse(actualTask2.isPresent(), "Task should not be present in the repository");
    }

    @Test
    public void testFindingTaskByCategory(){
        Task expectedTask = createTask();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        taskRepo.save(expectedTask);

        List<Task> actualTaskList = taskRepo.findByCategory(expectedTask.getTaskCategory());

        Assertions.assertEquals(1, actualTaskList.size(), "There should be exactly 1 task in the repository");
        Assertions.assertTrue(actualTaskList.stream()
                        .anyMatch(actualTask -> actualTask.getTaskID().equals(expectedTask.getTaskID()) &&
                                actualTask.getTaskTitle().equals(expectedTask.getTaskTitle()) &&
                                actualTask.getTaskDescription().equals(expectedTask.getTaskDescription()) &&
                                actualTask.getTaskStatus().equals(expectedTask.getTaskStatus()) &&
                                sdf.format(actualTask.getTaskDate()).equals(sdf.format(expectedTask.getTaskDate())) &&
                                actualTask.getTaskHours().equals(expectedTask.getTaskHours()) &&
                                actualTask.getTaskCategory().equals(expectedTask.getTaskCategory()) &&
                                actualTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getVersion().equals(expectedTask.getVersion())),
                "Task should be present in the repository");
    }

    @Test
    public void testFindingTaskByStatus() {
        Task expectedTask = createTask();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        taskRepo.save(expectedTask);

        List<Task> actualTaskList = taskRepo.findByStatus(expectedTask.getTaskStatus().toString());

        Assertions.assertEquals(1, actualTaskList.size(), "There should be exactly 1 task in the repository");
        Assertions.assertTrue(actualTaskList.stream()
                        .anyMatch(actualTask -> actualTask.getTaskID().equals(expectedTask.getTaskID()) &&
                                actualTask.getTaskTitle().equals(expectedTask.getTaskTitle()) &&
                                actualTask.getTaskDescription().equals(expectedTask.getTaskDescription()) &&
                                actualTask.getTaskStatus().equals(expectedTask.getTaskStatus()) &&
                                sdf.format(actualTask.getTaskDate()).equals(sdf.format(expectedTask.getTaskDate())) &&
                                actualTask.getTaskHours().equals(expectedTask.getTaskHours()) &&
                                actualTask.getTaskCategory().equals(expectedTask.getTaskCategory()) &&
                                actualTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getVersion().equals(expectedTask.getVersion())),
                "Task should be present in the repository");
    }

    @Test
    public void testFindingAllTask() {
        Task expectedTask = createTask();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime expectedDate = LocalDateTime.now();

        taskRepo.save(expectedTask);

        Task expectedTask2 = expectedTask;
        expectedTask2.setTaskID(UUID.fromString("e68042f8-3233-47fc-b31b-c5feb06eb95a"));
        expectedTask2.setTaskStatus(Status.COMPLETE);
        expectedTask2.setTaskTitle("Test Task 2");
        expectedTask2.setTaskDescription("Test Task 2");
        expectedTask2.setTaskHours(3.3F);
        expectedTask2.setCreatedAt(expectedDate);
        expectedTask2.setUpdatedAt(expectedDate);

        taskRepo.save(expectedTask2);

        int limit = 10;
        int offset = 0;

        List<Task> actualTaskList = taskRepo.findAll(limit, offset);
        Assertions.assertEquals(2, actualTaskList.size(), "There should be exactly 2 task in the repository");

        Assertions.assertTrue(actualTaskList.stream()
                        .anyMatch(actualTask -> actualTask.getTaskID().equals(expectedTask.getTaskID()) &&
                                actualTask.getTaskTitle().equals(expectedTask.getTaskTitle()) &&
                                actualTask.getTaskDescription().equals(expectedTask.getTaskDescription()) &&
                                actualTask.getTaskStatus().equals(expectedTask.getTaskStatus()) &&
                                sdf.format(actualTask.getTaskDate()).equals(sdf.format(expectedTask.getTaskDate())) &&
                                actualTask.getTaskHours().equals(expectedTask.getTaskHours()) &&
                                actualTask.getTaskCategory().equals(expectedTask.getTaskCategory()) &&
                                actualTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getVersion().equals(expectedTask.getVersion())),
                "Task should be present in the repository");

        Assertions.assertTrue(actualTaskList.stream()
                        .anyMatch(actualTask -> actualTask.getTaskID().equals(expectedTask2.getTaskID()) &&
                                actualTask.getTaskTitle().equals(expectedTask2.getTaskTitle()) &&
                                actualTask.getTaskDescription().equals(expectedTask2.getTaskDescription()) &&
                                actualTask.getTaskStatus().equals(expectedTask2.getTaskStatus()) &&
                                sdf.format(actualTask.getTaskDate()).equals(sdf.format(expectedTask2.getTaskDate())) &&
                                actualTask.getTaskHours().equals(expectedTask2.getTaskHours()) &&
                                actualTask.getTaskCategory().equals(expectedTask2.getTaskCategory()) &&
                                actualTask.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask2.getCreatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString().equals(expectedTask2.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS).toString()) &&
                                actualTask.getVersion().equals(expectedTask2.getVersion())),
                "Task should be present in the repository");
    }

    public Category createCategory(){
        Category expectedCategory = Category.builder()
                .categoryID(UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"))
                .categoryTitle("Test Category Repo")
                .categoryColour("#CBDFBC")
                .accountID(UUID.fromString("5199a3a2-81ca-45d7-88e4-62310dcc09e1"))
                .build();

        LocalDateTime expectedDate = LocalDateTime.now();

        expectedCategory.setCreatedAt(expectedDate);
        expectedCategory.setUpdatedAt(expectedDate);
        expectedCategory.setVersion(1L);

        return expectedCategory;
    }

    public Task createTask(){
        Category expectedCategory = createCategory();
        categoryRepo.save(expectedCategory);

        Task task = Task.builder()
                .taskID(UUID.fromString("09f535f1-d205-4cf0-b5fe-b31be8605b9f"))
                .taskTitle("Test Task Repo")
                .taskDescription("Test Task Description")
                .taskStatus(Status.TODO)
                .taskDate(Date.valueOf("2024-11-13"))
                .taskHours(4.5F)
                .taskCategory(expectedCategory.getCategoryID())
                .build();

        LocalDateTime expectedDate = LocalDateTime.now();

        task.setCreatedAt(expectedDate);
        task.setUpdatedAt(expectedDate);
        task.setVersion(1L);

        return task;
    }
}
