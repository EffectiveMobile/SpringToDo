package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.Status;
import com.emobile.springtodo.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    private final String BASE_URL = "/api/task";

    @Test
    @DisplayName("GET /api/task/showAll - Получение всех задач с пагинацией")
    void showAllTasks() throws Exception {
        //given
        String expectedJson = """
                {
                            "content": [
                                {
                                    "title": "Task 1",
                                    "description": "Description 1",
                                    "status": "PENDING"
                                },
                                {
                                    "title": "Task 2",
                                    "description": "Description 2",
                                    "status": "IN_PROGRESS"
                                }
                            ],
                            "pageable": {
                                "sort": {
                                    "empty": true,
                                    "sorted": false,
                                    "unsorted": true
                                },
                                "offset": 0,
                                "pageNumber": 0,
                                "pageSize": 2,
                                "paged": true,
                                "unpaged": false
                            },
                            "last": true,
                            "totalPages": 1,
                            "totalElements": 2,
                            "size": 2,
                            "number": 0,
                            "sort": {
                                "empty": true,
                                "sorted": false,
                                "unsorted": true
                            },
                            "first": true,
                            "numberOfElements": 2,
                            "empty": false
                        }
                """;

        Page<TaskDto> tasks = new PageImpl<>(Arrays.asList(
                new TaskDto("Task 1", "Description 1", "PENDING"),
                new TaskDto("Task 2", "Description 2", "IN_PROGRESS")
        ), PageRequest.of(0, 2), 2);
        when(taskService.getAllTasks(2, 0)).thenReturn(tasks);

        //when
        String actualJson = mockMvc.perform(get(BASE_URL + "/showAll")
                        .param("offset", "0")
                        .param("limit", "2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

    @Test
    @DisplayName("GET /api/task/show/byTitle - Поиск задач по названию")
    void showTaskByTitleTest() throws Exception {
        //given
        String expectedJson = """
                {
                            "content": [
                                {
                                    "title": "Task 1",
                                    "description": "Description 1",
                                    "status": "PENDING"
                                },
                                {
                                    "title": "Task 2",
                                    "description": "Description 2",
                                    "status": "IN_PROGRESS"
                                }
                            ],
                            "pageable": {
                                "sort": {
                                    "empty": true,
                                    "sorted": false,
                                    "unsorted": true
                                },
                                "offset": 0,
                                "pageNumber": 0,
                                "pageSize": 2,
                                "paged": true,
                                "unpaged": false
                            },
                            "last": true,
                            "totalPages": 1,
                            "totalElements": 2,
                            "size": 2,
                            "number": 0,
                            "sort": {
                                "empty": true,
                                "sorted": false,
                                "unsorted": true
                            },
                            "first": true,
                            "numberOfElements": 2,
                            "empty": false
                        }
                """;

        Page<TaskDto> tasks = new PageImpl<>(Arrays.asList(
                new TaskDto("Task 1", "Description 1", "PENDING"),
                new TaskDto("Task 2", "Description 2", "IN_PROGRESS")
        ), PageRequest.of(0, 2), 2);
        when(taskService.getTaskByTitle("Task", 2, 0)).thenReturn(tasks);

        //when
        String actualJson = mockMvc.perform(get(BASE_URL + "/show/byTitle")
                        .param("offset", "0")
                        .param("limit", "2")
                        .param("title", "Task"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

    @Test
    @DisplayName("DELETE /api/task/delete/{title} - Удаление задачи по названию")
    void deleteTaskTest() throws Exception {
        doNothing().when(taskService).deleteTaskByTitle("Task");

        //when-then
        mockMvc.perform(delete(BASE_URL + "/delete/Task"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted!"));
        verify(taskService, times(1)).deleteTaskByTitle("Task");
    }

    @Test
    @DisplayName("POST /api/task/create - Создание новой задачи")
    void createTaskTest() throws Exception {
        // given
        TaskDto taskDto = new TaskDto("Task", "Desc", "PENDING");
        doNothing().when(taskService).createTask(taskDto);

        //when-then
        mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON.getMediaType())
                        .content("{\"title\":\"Task\",\"description\":\"Desc\",\"status\":\"PENDING\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Task created!"));
        verify(taskService, times(1)).createTask(taskDto);
    }

    @Test
    @DisplayName("PATCH /api/task/edit/{title}/status - Изменение статуса задачи")
    void editStatusTest() throws Exception {
        // given
        doNothing().when(taskService).editStatus("Task", Status.COMPLETED);

        //when-then
        mockMvc.perform(patch(BASE_URL + "/edit/Task/status")
                        .param("newStatus", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Status has been changed!"));
        verify(taskService, times(1)).editStatus("Task", Status.COMPLETED);
    }

    @Test
    @DisplayName("GET /api/task/show/byStatus - Получение задач по статусу")
    void getTasksByStatusTest() throws Exception {
        //given
        String expectedJson = """
                {
                            "content": [
                                {
                                    "title": "Task 1",
                                    "description": "Description 1",
                                    "status": "PENDING"
                                },
                                {
                                    "title": "Task 2",
                                    "description": "Description 2",
                                    "status": "IN_PROGRESS"
                                }
                            ],
                            "pageable": {
                                "sort": {
                                    "empty": true,
                                    "sorted": false,
                                    "unsorted": true
                                },
                                "offset": 0,
                                "pageNumber": 0,
                                "pageSize": 2,
                                "paged": true,
                                "unpaged": false
                            },
                            "last": true,
                            "totalPages": 1,
                            "totalElements": 2,
                            "size": 2,
                            "number": 0,
                            "sort": {
                                "empty": true,
                                "sorted": false,
                                "unsorted": true
                            },
                            "first": true,
                            "numberOfElements": 2,
                            "empty": false
                        }
                """;

        Page<TaskDto> tasks = new PageImpl<>(Arrays.asList(
                new TaskDto("Task 1", "Description 1", "PENDING"),
                new TaskDto("Task 2", "Description 2", "IN_PROGRESS")
        ), PageRequest.of(0, 2), 2);
        when(taskService.getTasksByStatus(Status.PENDING, 2, 0)).thenReturn(tasks);

        //when
        String actualJson = mockMvc.perform(get(BASE_URL + "/show/byStatus")
                        .param("offset", "0")
                        .param("limit", "2")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }
}