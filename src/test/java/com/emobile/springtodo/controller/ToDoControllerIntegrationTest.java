package com.emobile.springtodo.controller;


import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.service.ToDoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты для контроллера ToDo.
 * Проверяются основные операции с задачами (создание, получение).
 */
@ExtendWith(SpringExtension.class)
@Import(ToDoServiceImpl.class)
class ToDoControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateToDo() {
        ToDoItemDto dto = new ToDoItemDto(1L, "New Task", "Description of the task", false);


        ToDoItemDto response = restTemplate.postForObject("http://localhost:" + port + "/api/todos", dto, ToDoItemDto.class);


        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getDescription()).isEqualTo(dto.getDescription());
        assertThat(response.isCompleted()).isEqualTo(dto.isCompleted());
    }

    @Test
    void testGetAllToDos() {

        ToDoItemDto dto = new ToDoItemDto(1L, "New Task", "Description of the task", false);
        restTemplate.postForObject("http://localhost:" + port + "/api/todos", dto, ToDoItemDto.class);


        ToDoItemDto[] response = restTemplate.getForObject("http://localhost:" + port + "/api/todos", ToDoItemDto[].class);


        assertThat(response).isNotEmpty();
    }

    @Test
    void testGetByIdToDo() {

        ToDoItemDto dto = new ToDoItemDto(1L, "New Task", "Description of the task", false);
        ToDoItemDto createdDto = restTemplate.postForObject("http://localhost:" + port + "/api/todos", dto, ToDoItemDto.class);


        ToDoItemDto response = restTemplate.getForObject("http://localhost:" + port + "/api/todos/" + createdDto.getId(), ToDoItemDto.class);


        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(dto.getTitle());
        assertThat(response.getDescription()).isEqualTo(dto.getDescription());
        assertThat(response.isCompleted()).isEqualTo(dto.isCompleted());
    }


}