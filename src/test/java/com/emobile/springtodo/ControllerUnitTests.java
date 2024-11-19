package com.emobile.springtodo;

import com.emobile.springtodo.api.controller.CategoryController;
import com.emobile.springtodo.api.controller.TaskController;
import com.emobile.springtodo.api.request.CategoryCreationRequest;
import com.emobile.springtodo.api.request.CategoryUpdateRequest;
import com.emobile.springtodo.api.request.TaskCreationRequest;
import com.emobile.springtodo.api.request.TaskUpdateRequest;
import com.emobile.springtodo.core.entity.Category;
import com.emobile.springtodo.core.entity.Status;
import com.emobile.springtodo.core.entity.Task;
import com.emobile.springtodo.core.service.CategoryService;
import com.emobile.springtodo.core.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest({CategoryController.class, TaskController.class})
public class ControllerUnitTests {
    @MockBean
    CategoryService categoryService;

    @MockBean
    TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


//  Unit-Testing Category Controller

    @Test
    public void testCategoryControllerGetAll() throws Exception {
        Category category1 = new Category(UUID.randomUUID(), "Test Category1 Controller",
                "#CBDFBC",  UUID.fromString("5199a3a2-81ca-45d7-88e4-62310dcc09e1"));

        Category category2 = new Category(UUID.randomUUID(), "Test Category2 Controller",
                "#BBDFBB",  UUID.fromString("5199a3a2-81ca-45d7-88e4-62310dcc09e1"));

        List<Category> categories = List.of(category1, category2);

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/category/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(categories.size()))
                .andExpect(jsonPath("$[0].categoryTitle").value("Test Category1 Controller"))
                .andExpect(jsonPath("$[1].categoryTitle").value("Test Category2 Controller"));
    }

    @Test
    public void testCategoryControllerGetByCategoryId() throws Exception {
        Category category = new Category(UUID.randomUUID(), "Test Category Controller",
                "#CBDFBC",  UUID.fromString("5199a3a2-81ca-45d7-88e4-62310dcc09e1"));

        when(categoryService.getById(category.getCategoryID().toString())).thenReturn(category);

        mockMvc.perform(get("/category/" + category.getCategoryID().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryTitle").value("Test Category Controller"))
                .andExpect(jsonPath("$.categoryColour").value("#CBDFBC"));
    }

    @Test
    public void testCategoryControllerGetByAccountId() throws Exception {
        Category category1 = new Category(UUID.randomUUID(), "Test Category1 Controller",
                "#CBDFBC",  UUID.fromString("5199a3a2-81ca-45d7-88e4-62310dcc09e1"));

        Category category2 = new Category(UUID.randomUUID(), "Test Category2 Controller",
                "#BBDFBB",  UUID.fromString("5199a3a2-81ca-45d7-88e4-62310dcc09e1"));

        List<Category> categories = List.of(category1, category2);

        when(categoryService.getByAccountId(category1.getAccountID().toString())).thenReturn(categories);

        mockMvc.perform(get("/category/account/" + category1.getAccountID().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(categories.size()))
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
        UUID categoryID = UUID.randomUUID();

        CategoryUpdateRequest request = new CategoryUpdateRequest(
                "Updated Test Category Service",
                "#CBDFBF"
        );

        mockMvc.perform(put("/category/{categoryid}", categoryID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCategoryBadRequest() throws Exception {
        UUID categoryId = UUID.randomUUID();
        CategoryUpdateRequest request = new CategoryUpdateRequest(null, null);  // Both fields are null, should trigger a bad request

        mockMvc.perform(put("/category/{categoryid}", categoryId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Request body cannot be empty"));
    }

    @Test
    public void testCategoryControllerDeleteCategory() throws Exception {
        UUID categoryID = UUID.randomUUID();

        mockMvc.perform(delete("/category/{categoryid}", categoryID.toString()))
                .andExpect(status().isNoContent());
    }


//  Unit-Testing Task Controller

    @Test
    public void testTaskControllerGetAll() throws Exception {
        Task task1 = new Task(UUID.randomUUID(), "Test Task1 Controller",  "Test Task1 Description",
                Status.TODO, Date.valueOf("2024-11-13"), 4.5F,  UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));
        Task task2 = new Task(UUID.randomUUID(), "Test Task2 Controller", "Test Task2 Description",
                Status.COMPLETE, Date.valueOf("2024-11-16"), 10F, UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));

        List<Task> tasks = List.of(task1, task2);
        int limit = 10;
        int offset = 0;

        when(taskService.getAllTasks(limit, offset)).thenReturn(tasks);

        mockMvc.perform(get("/task/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(tasks.size()))
                .andExpect(jsonPath("$[0].taskTitle").value("Test Task1 Controller"))
                .andExpect(jsonPath("$[1].taskTitle").value("Test Task2 Controller"));
    }

    @Test
    public void testTaskControllerGetByTaskId() throws Exception {
        Task task = new Task(UUID.randomUUID(), "Test Task Controller",  "Test Task Description",
                Status.TODO, Date.valueOf("2024-11-13"), 4.5F,  UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));

        when(taskService.getTaskById(task.getTaskID().toString())).thenReturn(task);

        mockMvc.perform(get("/task/{taskid}", task.getTaskID().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskTitle").value("Test Task Controller"))
                .andExpect(jsonPath("$.taskDescription").value("Test Task Description"))
                .andExpect(jsonPath("$.taskStatus").value("TODO"));
    }

    @Test
    public void testTaskControllerGetByCategory() throws Exception {
        Task task1 = new Task(UUID.randomUUID(), "Test Task1 Controller",  "Test Task1 Description",
                Status.TODO, Date.valueOf("2024-11-13"), 4.5F,  UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));
        Task task2 = new Task(UUID.randomUUID(), "Test Task2 Controller", "Test Task2 Description",
                Status.COMPLETE, Date.valueOf("2024-11-16"), 10F, UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));

        List<Task> tasks = List.of(task1, task2);

        when(taskService.getByCategory(task1.getTaskCategory().toString())).thenReturn(tasks);

        mockMvc.perform(get("/task/category/{categoryid}", task1.getTaskCategory().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].taskTitle").value("Test Task1 Controller"))
                .andExpect(jsonPath("$[1].taskTitle").value("Test Task2 Controller"));

    }

    @Test
    public void testTaskControllerGetByStatus() throws Exception {
        Task task1 = new Task(UUID.randomUUID(), "Test Task1 Controller",  "Test Task1 Description",
                Status.COMPLETE, Date.valueOf("2024-11-13"), 4.5F,  UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));
        Task task2 = new Task(UUID.randomUUID(), "Test Task2 Controller", "Test Task2 Description",
                Status.COMPLETE, Date.valueOf("2024-11-16"), 10F, UUID.fromString("2cb7304c-0116-464c-94e5-e4ea8b799460"));

        List<Task> tasks = List.of(task1, task2);

        when(taskService.getByStatus(task1.getTaskStatus().toString())).thenReturn(tasks);

        mockMvc.perform(get("/task/status/{status}", task1.getTaskStatus().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].taskTitle").value("Test Task1 Controller"))
                .andExpect(jsonPath("$[1].taskTitle").value("Test Task2 Controller"));

    }

    @Test
    public void testTaskControllerCreateTask() throws Exception {
        TaskCreationRequest request = new TaskCreationRequest(
                "Test Task Service",
                "Test Task Description",
                "TODO",
                Date.valueOf("2024-11-13"),
                4.5F,
                "2cb7304c-0116-464c-94e5-e4ea8b799460");

        mockMvc.perform(post("/task/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testTaskControllerUpdateTask() throws Exception {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Updated Task Title",
                "Updated Task Description",
                "DELAYED",
                Date.valueOf("2024-11-16"),
                10F,
                "2cb7304c-0116-464c-94e5-e4ea8b799460"
        );

        mockMvc.perform(put("/task/{taskId}", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testTaskControllerUpdateTaskFailure() throws Exception {
        TaskUpdateRequest request = new TaskUpdateRequest(null, null, null, null, null, null);

        mockMvc.perform(put("/task/{taskId}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Request body cannot be empty"));
    }

    @Test
    public void testTaskControllerDeleteTask() throws Exception {
        mockMvc.perform(delete("/task/{taskId}", UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());
    }

}
