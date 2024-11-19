package com.emobile.springtodo;

import com.emobile.springtodo.api.request.CategoryCreationRequest;
import com.emobile.springtodo.api.request.CategoryUpdateRequest;
import com.emobile.springtodo.api.request.TaskCreationRequest;
import com.emobile.springtodo.api.request.TaskUpdateRequest;
import com.emobile.springtodo.core.DAO.CategoryDAO;
import com.emobile.springtodo.core.DAO.TaskDAO;
import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.entity.Status;
import com.emobile.springtodo.core.entity.Task;
import com.emobile.springtodo.core.exception.customexceptions.CategoryCreationException;
import com.emobile.springtodo.core.exception.customexceptions.CategoryDeletionException;
import com.emobile.springtodo.core.exception.customexceptions.TaskCreationException;
import com.emobile.springtodo.core.exception.customexceptions.TaskDeletionException;
import com.emobile.springtodo.core.service.CategoryService;
import com.emobile.springtodo.core.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTests {

    @Mock
    CategoryDAO categoryDAO;

    @Mock
    RedisCacheManager cacheManager;

    @Mock
    Cache cache;

    @Mock
    TaskDAO taskDAO;

    @InjectMocks
    CategoryService categoryService;

    @InjectMocks
    TaskService taskService;

    private static final int LIMIT = 10;
    private static final int OFFSET = 0;

//  Testing Category Service

    @Test
    public void testCreateCategorySuccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CategoryCreationRequest requestOfExpectedCategory = createCategoryRequest();
        Category expectedCategory = Category.builder()
                .categoryID(any(UUID.class))
                .categoryTitle("Test Category Service")
                .categoryColour("#CBDFBC")
                .accountID(UUID.fromString(requestOfExpectedCategory.accountID()))
                .build();

        categoryService.createCategory(requestOfExpectedCategory);

        verify(categoryDAO, times(1)).createCategory(argThat(category ->
                category.getCategoryTitle().equals(expectedCategory.getCategoryTitle()) &&
                category.getCategoryColour().equals(expectedCategory.getCategoryColour()) &&
                category.getAccountID().equals(expectedCategory.getAccountID())
        ));
    }

    @Test
    public void testCreateCategoryFailure(){
        CategoryCreationRequest requestOfExpectedCategory = createCategoryRequest();

        doThrow(DataIntegrityViolationException.class).when(categoryDAO).createCategory(any(Category.class));

        Assertions.assertThrows(CategoryCreationException.class, () -> {
            categoryService.createCategory(requestOfExpectedCategory);
        });
    }

    @Test
    public void testUpdateCategory() {
        CategoryUpdateRequest requestOfExpectedCategory = updateCategoryRequest();

        Category expectedCategory = createCategory();

        when(categoryDAO.getById(expectedCategory.getCategoryID())).thenReturn(expectedCategory);

        categoryService.updateCategory(expectedCategory.getCategoryID().toString(), requestOfExpectedCategory);

        verify(categoryDAO, times(1)).updateCategory(argThat(category ->
                category.getCategoryID().equals(expectedCategory.getCategoryID()) &&
                category.getCategoryTitle().equals(requestOfExpectedCategory.categoryTitle()) &&
                category.getCategoryColour().equals(requestOfExpectedCategory.categoryColour()) &&
                !category.getUpdatedAt().equals(category.getCreatedAt()) &&
                category.getVersion().equals(2L)
                ), eq(1L));
    }

    @Test
    public void testUpdateCategoryFailure(){
        CategoryUpdateRequest requestOfExpectedCategory = updateCategoryRequest();
        Category expectedCategory = createCategory();

        when(categoryDAO.getById(expectedCategory.getCategoryID())).thenReturn(expectedCategory);
        doThrow(OptimisticLockingFailureException.class).when(categoryDAO).updateCategory(any(Category.class), anyLong());

        Assertions.assertThrows(ConcurrentModificationException.class, () -> {
            categoryService.updateCategory(expectedCategory.getCategoryID().toString(), requestOfExpectedCategory);
        });
    }

    @Test
    public void testDeleteCategorySuccess() {
        Category expectedCategory = createCategory();

        when(categoryDAO.getById(expectedCategory.getCategoryID())).thenReturn(expectedCategory);

        categoryService.deleteCategory(expectedCategory.getCategoryID().toString());

        verify(categoryDAO, times(1)).deleteCategory(argThat(categoryId ->
                categoryId.equals(expectedCategory.getCategoryID())
        ));
    }

    @Test
    public void testDeleteCategoryFailure() {
        Category expectedCategory = createCategory();

        when(categoryDAO.getById(expectedCategory.getCategoryID())).thenReturn(expectedCategory);
        doThrow(DataIntegrityViolationException.class).when(categoryDAO).deleteCategory(any());

        Assertions.assertThrows(CategoryDeletionException.class, () -> {
            categoryService.deleteCategory(expectedCategory.getCategoryID().toString());
        });
    }

    @Test
    public void testGetAllCategories() {
        Category expectedCategory = createCategory();
        Category expectedCategory2 = createCategory();
        expectedCategory2.setCategoryTitle("Test Category Service2");
        expectedCategory2.setCategoryColour("#CBDFBB");
        List<Category> expectedCategories = List.of(expectedCategory, expectedCategory2);

        when(categoryDAO.getAll()).thenReturn(expectedCategories);

        List<Category> actualCategories = categoryService.findAll();

        Assertions.assertEquals(expectedCategories.size(), actualCategories.size());
        Assertions.assertEquals(expectedCategories.getFirst().getCategoryID(), actualCategories.getFirst().getCategoryID());
        Assertions.assertEquals(expectedCategories.getLast().getCategoryID(), actualCategories.getLast().getCategoryID());
        Assertions.assertEquals(expectedCategories.getFirst().getCategoryTitle(), actualCategories.getFirst().getCategoryTitle());
        Assertions.assertEquals(expectedCategories.getLast().getCategoryTitle(), actualCategories.getLast().getCategoryTitle());
        Assertions.assertEquals(expectedCategories.getFirst().getCategoryColour(), actualCategories.getFirst().getCategoryColour());
        Assertions.assertEquals(expectedCategories.getLast().getCategoryColour(), actualCategories.getLast().getCategoryColour());
        verify(categoryDAO, times(1)).getAll();
    }

    @Test
    public void testGetCategoryById() {
        Category expectedCategory = createCategory();

        when(categoryDAO.getById(expectedCategory.getCategoryID())).thenReturn(expectedCategory);

        Category actualCategory = categoryService.getById(expectedCategory.getCategoryID().toString());

        Assertions.assertEquals(expectedCategory.getCategoryID(), actualCategory.getCategoryID());
        Assertions.assertEquals(expectedCategory.getCategoryTitle(), actualCategory.getCategoryTitle());
        Assertions.assertEquals(expectedCategory.getCategoryColour(), actualCategory.getCategoryColour());

        verify(categoryDAO, times(1)).getById(expectedCategory.getCategoryID());
    }

    @Test
    public void testGetCategoryByAccountID() {
        Category expectedCategory = createCategory();
        Category expectedCategory2 = createCategory();
        expectedCategory2.setCategoryTitle("Test Category Service2");
        expectedCategory2.setCategoryColour("#CBDFBB");
        List<Category> expectedCategories = List.of(expectedCategory, expectedCategory2);

        when(categoryDAO.getByAccountId(expectedCategory.getAccountID())).thenReturn(expectedCategories);

        List<Category> actualCategories = categoryService.getByAccountId(expectedCategory.getAccountID().toString());

        Assertions.assertEquals(expectedCategories.size(), actualCategories.size());
        Assertions.assertEquals(expectedCategories.getFirst().getCategoryID(), actualCategories.getFirst().getCategoryID());
        Assertions.assertEquals(expectedCategories.getLast().getCategoryID(), actualCategories.getLast().getCategoryID());
        Assertions.assertEquals(expectedCategories.getFirst().getCategoryTitle(), actualCategories.getFirst().getCategoryTitle());
        Assertions.assertEquals(expectedCategories.getLast().getCategoryTitle(), actualCategories.getLast().getCategoryTitle());
        Assertions.assertEquals(expectedCategories.getFirst().getCategoryColour(), actualCategories.getFirst().getCategoryColour());
        Assertions.assertEquals(expectedCategories.getLast().getCategoryColour(), actualCategories.getLast().getCategoryColour());
        verify(categoryDAO, times(1)).getByAccountId(expectedCategory.getAccountID());
    }


//  Testing Task Service

    @Test
    public void testCreateTaskSuccess() {
        TaskCreationRequest requestOfExpectedTask = createTaskRequest();

        Task expectedTask = Task.builder()
                .taskID(any(UUID.class))
                .taskTitle("Test Task Service")
                .taskDescription("Test Task Description")
                .taskStatus(Status.TODO)
                .taskDate(Date.valueOf("2024-11-13"))
                .taskHours(4.5F)
                .taskCategory(UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"))
                .build();

        Category expectedCategory = createCategory();

        when(categoryDAO.getById(UUID.fromString(requestOfExpectedTask.taskCategory()))).thenReturn(expectedCategory);

        taskService.createTask(requestOfExpectedTask);

        verify(taskDAO, times(1)).createTask(argThat(task ->
                task.getTaskTitle().equals(expectedTask.getTaskTitle()) &&
                task.getTaskDescription().equals(expectedTask.getTaskDescription()) &&
                task.getTaskStatus().equals(expectedTask.getTaskStatus()) &&
                task.getTaskDate().equals(expectedTask.getTaskDate()) &&
                task.getTaskHours().equals(expectedTask.getTaskHours()) &&
                task.getTaskCategory().equals(expectedTask.getTaskCategory())
        ));
    }

    @Test
    public void testCreateTaskFailure() {
        TaskCreationRequest requestOfExpectedTask = createTaskRequest();

        Category expectedCategory = createCategory();

        when(categoryDAO.getById(UUID.fromString(requestOfExpectedTask.taskCategory()))).thenReturn(expectedCategory);
        doThrow(DataIntegrityViolationException.class).when(taskDAO).createTask(any(Task.class));

        Assertions.assertThrows(TaskCreationException.class, () -> {
            taskService.createTask(requestOfExpectedTask);
        });
    }

    @Test
    public void testUpdateTaskSuccess() {
        TaskUpdateRequest requestOfExpectedTask = updateTaskRequest();

        Task expectedTask = createTask();
        Category expectedCategory = createCategory();

        when(categoryDAO.getById(expectedCategory.getCategoryID())).thenReturn(expectedCategory);
        when(taskDAO.getById(expectedTask.getTaskID())).thenReturn(expectedTask);

        taskService.updateTask(expectedTask.getTaskID().toString(), requestOfExpectedTask);

        verify(taskDAO, times(1)).updateTask(argThat(task ->
                    task.getTaskID().equals(expectedTask.getTaskID()) &&
                    task.getTaskTitle().equals(requestOfExpectedTask.taskTitle()) &&
                    task.getTaskDescription().equals(requestOfExpectedTask.taskDescription()) &&
                    task.getTaskStatus().toString().equals(requestOfExpectedTask.taskStatus()) &&
                    task.getTaskDate().equals(requestOfExpectedTask.taskDate()) &&
                    task.getTaskHours().equals(requestOfExpectedTask.taskHours()) &&
                    task.getTaskCategory().toString().equals(requestOfExpectedTask.taskCategory())
                ), eq(1L));
    }

    @Test
    public void testUpdateTaskFailure() {
        TaskUpdateRequest requestOfExpectedTask = updateTaskRequest();
        Category expectedCategory = createCategory();
        Task expectedTask = createTask();

        when(categoryDAO.getById(expectedCategory.getCategoryID())).thenReturn(expectedCategory);
        when(taskDAO.getById(expectedTask.getTaskID())).thenReturn(expectedTask);
        doThrow(OptimisticLockingFailureException.class).when(taskDAO).updateTask(any(Task.class), anyLong());

        Assertions.assertThrows(ConcurrentModificationException.class, () -> {
            taskService.updateTask(expectedTask.getTaskID().toString(), requestOfExpectedTask);
        });
    }

    @Test
    public void testDeleteTaskSuccess() {
        Task expectedTask = createTask();

        when(taskDAO.getById(expectedTask.getTaskID())).thenReturn(expectedTask);

        taskService.deleteTask(expectedTask.getTaskID().toString());

        verify(taskDAO, times(1)).deleteTask(argThat(taskId ->
                taskId.equals(expectedTask.getTaskID())
        ));
    }

    @Test
    public void testDeleteTaskFailure() {
        Task expectedTask = createTask();

        when(taskDAO.getById(expectedTask.getTaskID())).thenReturn(expectedTask);
        doThrow(DataIntegrityViolationException.class).when(taskDAO).deleteTask(any());

        Assertions.assertThrows(TaskDeletionException.class, () -> {
            taskService.deleteTask(expectedTask.getTaskID().toString());
        });
    }

    @Test
    public void testGetAllTasks() {
        Task expectedTask = createTask();
        Task expectedTask2 = expectedTask;

        expectedTask2.setTaskTitle("Test Task Service2");
        expectedTask2.setTaskDescription("Test Task Description2");
        expectedTask2.setTaskStatus(Status.DELAYED);
        expectedTask2.setTaskDate(Date.valueOf("2024-11-16"));
        expectedTask2.setTaskHours(10F);

        List<Task> expectedTasks = List.of(expectedTask, expectedTask2);
        when(taskDAO.getAll(LIMIT, OFFSET)).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getAllTasks(LIMIT, OFFSET);

        Assertions.assertEquals(expectedTasks.size(), actualTasks.size());
        Assertions.assertEquals(expectedTasks.getFirst().getTaskID(), actualTasks.getFirst().getTaskID());
        Assertions.assertEquals(expectedTasks.getFirst().getTaskTitle(), actualTasks.getFirst().getTaskTitle());
        Assertions.assertEquals(expectedTasks.getFirst().getTaskDescription(), actualTasks.getFirst().getTaskDescription());
        Assertions.assertEquals(expectedTasks.getLast().getTaskStatus(), actualTasks.getLast().getTaskStatus());
        Assertions.assertEquals(expectedTasks.getLast().getTaskDate(), actualTasks.getLast().getTaskDate());
        Assertions.assertEquals(expectedTasks.getLast().getTaskHours(), actualTasks.getLast().getTaskHours());
        Assertions.assertEquals(expectedTasks.getLast().getTaskCategory(), actualTasks.getLast().getTaskCategory());
        verify(taskDAO, times(1)).getAll(LIMIT, OFFSET);
    }

    @Test
    public void testGetTaskById() {
        Task expectedTask = createTask();

        when(taskDAO.getById(any(UUID.class))).thenReturn(expectedTask);

        Task actualTask = taskService.getTaskById(expectedTask.getTaskID().toString());

        Assertions.assertEquals(expectedTask.getTaskID(), actualTask.getTaskID());
        Assertions.assertEquals(expectedTask.getTaskTitle(), actualTask.getTaskTitle());
        Assertions.assertEquals(expectedTask.getTaskDescription(), actualTask.getTaskDescription());
        Assertions.assertEquals(expectedTask.getTaskStatus(), actualTask.getTaskStatus());
        Assertions.assertEquals(expectedTask.getTaskDate(), actualTask.getTaskDate());
        Assertions.assertEquals(expectedTask.getTaskHours(), actualTask.getTaskHours());
        Assertions.assertEquals(expectedTask.getTaskCategory(), actualTask.getTaskCategory());

        verify(taskDAO, times(1)).getById(any(UUID.class));
    }

    @Test
    public void testGetTasksByCategoryId() {
        Task expectedTask = createTask();
        Task expectedTask2 = expectedTask;

        expectedTask2.setTaskTitle("Test Task Service2");
        expectedTask2.setTaskDescription("Test Task Description2");
        expectedTask2.setTaskStatus(Status.DELAYED);
        expectedTask2.setTaskDate(Date.valueOf("2024-11-16"));
        expectedTask2.setTaskHours(10F);

        List<Task> expectedTasks = List.of(expectedTask, expectedTask2);

        Category expectedCategory = createCategory();

        when(categoryDAO.getById(any(UUID.class))).thenReturn(expectedCategory);
        when(taskDAO.getByCategory(any(UUID.class))).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getByCategory(expectedTask.getTaskCategory().toString());

        Assertions.assertEquals(expectedTasks.size(), actualTasks.size());
        Assertions.assertEquals(expectedTasks.getFirst().getTaskCategory(), actualTasks.getLast().getTaskCategory());
        Assertions.assertEquals(expectedTasks.getLast().getTaskCategory(), actualTasks.getLast().getTaskCategory());
        Assertions.assertEquals(actualTasks.getFirst().getTaskCategory(), actualTasks.getLast().getTaskCategory());
        verify(taskDAO, times(1)).getByCategory(expectedTask.getTaskCategory());
    }

    @Test
    public void testGetTasksByStatus() {
        Task expectedTask = createTask();
        Task expectedTask2 = expectedTask;

        expectedTask2.setTaskTitle("Test Task Service2");
        expectedTask2.setTaskDescription("Test Task Description2");
        expectedTask2.setTaskStatus(Status.TODO);
        expectedTask2.setTaskDate(Date.valueOf("2024-11-16"));
        expectedTask2.setTaskHours(10F);

        List<Task> expectedTasks = List.of(expectedTask, expectedTask2);

        when(taskDAO.getByStatus(any(String.class))).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getByStatus(expectedTask.getTaskStatus().toString());

        Assertions.assertEquals(expectedTasks.size(), actualTasks.size());
        Assertions.assertEquals(expectedTasks.getFirst().getTaskStatus(), actualTasks.getLast().getTaskStatus());
        Assertions.assertEquals(expectedTasks.getLast().getTaskStatus(), actualTasks.getLast().getTaskStatus());
        Assertions.assertEquals(actualTasks.getFirst().getTaskStatus(), actualTasks.getLast().getTaskStatus());
        verify(taskDAO, times(1)).getByStatus(expectedTask.getTaskStatus().toString());
    }

    public CategoryCreationRequest createCategoryRequest(){
        CategoryCreationRequest request = new CategoryCreationRequest(
                "Test Category Service",
                "#CBDFBC",
                "5199a3a2-81ca-45d7-88e4-62310dcc09e1"
        );

        return request;
    }

    public CategoryUpdateRequest updateCategoryRequest(){
        CategoryUpdateRequest request = new CategoryUpdateRequest(
                "Updated Test Category Service",
                "#CBDFBF"
        );

        return request;
    }

    public Category createCategory(){
        Category expectedCategory = Category.builder()
                .categoryID(UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"))
                .categoryTitle("Test Category Service")
                .categoryColour("#CBDFBC")
                .accountID(UUID.fromString("5199a3a2-81ca-45d7-88e4-62310dcc09e1"))
                .build();

        LocalDateTime expectedDate = LocalDateTime.now();

        expectedCategory.setCreatedAt(expectedDate);
        expectedCategory.setUpdatedAt(expectedDate);
        expectedCategory.setVersion(1L);

        return expectedCategory;
    }

    public TaskCreationRequest createTaskRequest() {
        TaskCreationRequest request = new TaskCreationRequest(
                "Test Task Service",
                "Test Task Description",
                "TODO",
                Date.valueOf("2024-11-13"),
                4.5F,
                "2cb7304c-0116-464c-94e5-e4ea8b799460"
        );

        return request;
    }

    public TaskUpdateRequest updateTaskRequest() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Updated Task Title",
                "Updated Task Description",
                "DELAYED",
                Date.valueOf("2024-11-16"),
                10F,
                "2cb7304c-0116-464c-94e5-e4ea8b799460"
        );

        return request;
    }

    public Task createTask(){
        Category expectedCategory = createCategory();

        Task task = Task.builder()
                .taskID(UUID.fromString("09f535f1-d205-4cf0-b5fe-b31be8605b9f"))
                .taskTitle("Test Task Service")
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
