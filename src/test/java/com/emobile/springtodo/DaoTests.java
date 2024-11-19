package com.emobile.springtodo;

import com.emobile.springtodo.core.DAO.CategoryDAO;
import com.emobile.springtodo.core.DAO.TaskDAO;
import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.entity.Status;
import com.emobile.springtodo.core.entity.Task;
import com.emobile.springtodo.core.exception.customexceptions.CategoryNotFoundException;
import com.emobile.springtodo.core.exception.customexceptions.CustomDataAccessException;
import com.emobile.springtodo.core.exception.customexceptions.TaskNotFoundException;
import com.emobile.springtodo.core.repository.CategoryRepoImpl;
import com.emobile.springtodo.core.repository.TaskRepoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DaoTests {

    @Mock
    private CategoryRepoImpl categoryRepo;

    @Mock
    private TaskRepoImpl taskRepo;

    @InjectMocks
    private CategoryDAO categoryDAO;

    @InjectMocks
    private TaskDAO taskDAO;

//  Testing Category DAO

    @Test
    public void testGetByIdSuccess() {
        Category expectedCategory = createCategory();
        UUID categoryId = expectedCategory.getCategoryID();

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        Category actualCategory = categoryDAO.getById(categoryId);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(categoryId, actualCategory.getCategoryID());
        verify(categoryRepo, times(1)).findById(categoryId);
    }

    @Test
    public void testGetByIdCategoryNotFound() {
        UUID category = UUID.randomUUID();

        when(categoryRepo.findById(category)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            categoryDAO.getById(category);
        });

        Assertions.assertEquals(String.format("Category with ID %s not found", category), exception.getMessage());
        verify(categoryRepo, times(1)).findById(category);
    }

    @Test
    public void testCreateCategorySuccess() {
        Category expectedCategory = createCategory();

        when(categoryRepo.save(expectedCategory)).thenReturn(1);

        categoryDAO.createCategory(expectedCategory);

        verify(categoryRepo, times(1)).save(expectedCategory);
    }

    @Test
    public void testCreateCategoryCustomDataAccess() {
        Category expectedCategory = createCategory();

        when(categoryRepo.save(expectedCategory)).thenReturn(0);

        CustomDataAccessException exception = Assertions.assertThrows(CustomDataAccessException.class, () -> {
            categoryDAO.createCategory(expectedCategory);
        });

        Assertions.assertEquals("Failed to save category: No rows affected", exception.getMessage());
        verify(categoryRepo, times(1)).save(expectedCategory);
    }

//  Testing Task DAO

    @Test
    public void testGetByIdTaskSuccess() {
        Task expectedTask = createTask();
        UUID taskId = expectedTask.getTaskID();

        when(taskRepo.findById(taskId)).thenReturn(Optional.of(expectedTask));

        Task actualTask = taskDAO.getById(taskId);

        Assertions.assertNotNull(actualTask);
        Assertions.assertEquals(taskId, actualTask.getTaskID());
        verify(taskRepo, times(1)).findById(taskId);
    }

    @Test
    public void testGetByIdTaskCategoryNotFound() {
        Task expectedTask = createTask();
        UUID taskId = expectedTask.getTaskID();

        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException exception = Assertions.assertThrows(TaskNotFoundException.class, () -> {
            taskDAO.getById(taskId);
        });

        Assertions.assertEquals(String.format("Task with ID %s not found", taskId), exception.getMessage());
        verify(taskRepo, times(1)).findById(taskId);
    }

    @Test
    public void testCreateTaskSuccess() {
        Task expectedTask = createTask();

        when(taskRepo.save(expectedTask)).thenReturn(1);

        taskDAO.createTask(expectedTask);

        verify(taskRepo, times(1)).save(expectedTask);
    }

    @Test
    public void testCreateTaskCustomDataAccess() {
        Task expectedTask = createTask();

        when(taskRepo.save(expectedTask)).thenReturn(0);

        CustomDataAccessException exception = Assertions.assertThrows(CustomDataAccessException.class, () -> {
            taskDAO.createTask(expectedTask);
        });

        Assertions.assertEquals("Failed to save task: No rows affected", exception.getMessage());
        verify(taskRepo, times(1)).save(expectedTask);
    }

    public Category createCategory(){
        Category expectedCategory = Category.builder()
                .categoryID(UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"))
                .categoryTitle("Test Category Dao")
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

        Task task = Task.builder()
                .taskID(UUID.fromString("09f535f1-d205-4cf0-b5fe-b31be8605b9f"))
                .taskTitle("Test Task Dao")
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
