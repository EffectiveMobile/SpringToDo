package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.exceptions.ErrorInputDataException;
import com.emobile.springtodo.mapper.TaskMapper;
import com.emobile.springtodo.model.Status;
import com.emobile.springtodo.model.TaskEntity;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("createTask - Успешное создание новой задачи")
    void createTaskSuccessTest() {
        // Given
        TaskDto dto = new TaskDto("title", "desc", "PENDING");
        TaskEntity entity = new TaskEntity(1, "title", "desc", Status.PENDING);

        when(taskRepository.existTaskByTitle(dto.getTitle())).thenReturn(false);
        when(taskMapper.toEntity(dto)).thenReturn(entity);

        // When
        taskService.createTask(dto);

        // Then
        verify(taskRepository).existTaskByTitle("title");
        verify(taskMapper).toEntity(dto);
        verify(taskRepository).createTask(entity);
    }

    @Test
    @DisplayName("createTask - Ошибка при создании задачи с существующим названием")
    void createTaskExistingTitleTest() {
        // Given
        TaskDto dto = new TaskDto("existing", "desc", "PENDING");
        when(taskRepository.existTaskByTitle(dto.getTitle())).thenReturn(true);

        // When - Then
        assertThrowsExactly(ErrorInputDataException.class,
                () -> taskService.createTask(dto));

        verify(taskRepository).existTaskByTitle("existing");
        verifyNoInteractions(taskMapper);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    @DisplayName("deleteTaskByTitle - Успешное удаление задачи")
    void deleteTaskSuccessTest() {
        // Given
        String title = "existing";
        when(taskRepository.existTaskByTitle(title)).thenReturn(true);

        // When
        taskService.deleteTaskByTitle(title);

        // Then
        verify(taskRepository).existTaskByTitle(title);
        verify(taskRepository).deleteTaskByTitle(title);
    }

    @Test
    @DisplayName("deleteTaskByTitle - Ошибка при удалении несуществующей задачи")
    void deleteNonExistingTaskTest() {
        // Given
        String title = "non-existing";
        when(taskRepository.existTaskByTitle(title)).thenReturn(false);

        // When - Then
        assertThrowsExactly(ErrorInputDataException.class,
                () -> taskService.deleteTaskByTitle(title));

        verify(taskRepository).existTaskByTitle(title);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    @DisplayName("editStatus - Успешное изменение статуса задачи")
    void editStatusSuccessTest() {
        // Given
        String title = "existing";
        Status newStatus = Status.COMPLETED;
        when(taskRepository.existTaskByTitle(title)).thenReturn(true);

        // When
        taskService.editStatus(title, newStatus);

        // Then
        verify(taskRepository).existTaskByTitle(title);
        verify(taskRepository).editStatus(title, newStatus);
    }

    @Test
    @DisplayName("editStatus - Ошибка при изменении статуса несуществующей задачи")
    void editStatusForNonExistingTaskTest() {
        // Given
        String title = "non-existing";
        when(taskRepository.existTaskByTitle(title)).thenReturn(false);

        // When - Then
        assertThrowsExactly(ErrorInputDataException.class,
                () -> taskService.editStatus(title, Status.PENDING));

        verify(taskRepository).existTaskByTitle(title);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    @DisplayName("getTaskByTitle - Получение задач по названию")
    void getTasksByTitleTest() {
        // Given
        String searchTitle = "title";
        int limit = 20;
        int offset = 0;

        TaskEntity entity1 = new TaskEntity(1, "title1", "desc", Status.PENDING);
        TaskEntity entity2 = new TaskEntity(2, "title2", "desc", Status.PENDING);
        TaskDto dto1 = new TaskDto("title1", "desc", "PENDING");
        TaskDto dto2 = new TaskDto("title2", "desc", "PENDING");

        when(taskRepository.findTaskByTitle(searchTitle, limit, offset))
                .thenReturn(List.of(entity1, entity2));
        when(taskMapper.toDtoList(List.of(entity1, entity2)))
                .thenReturn(List.of(dto1, dto2));

        // When
        List<TaskDto> result = taskService.getTaskByTitle(searchTitle, limit, offset).getContent();

        // Then
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(taskRepository).findTaskByTitle(searchTitle, limit, offset);
        verify(taskMapper).toDtoList(List.of(entity1, entity2));
    }

    @Test
    @DisplayName("getTasksByStatus - Получение задач по статусу")
    void getTasksByStatusTest() {
        // Given
        Status status = Status.PENDING;
        int limit = 20;
        int offset = 0;

        TaskEntity entity1 = new TaskEntity(1, "task1", "desc", Status.PENDING);
        TaskEntity entity2 = new TaskEntity(2, "task2", "desc", Status.PENDING);
        TaskDto dto1 = new TaskDto("task1", "desc", "PENDING");
        TaskDto dto2 = new TaskDto("task2", "desc", "PENDING");

        when(taskRepository.findTaskByStatus(status.name(), limit, offset))
                .thenReturn(List.of(entity1, entity2));
        when(taskMapper.toDtoList(List.of(entity1, entity2)))
                .thenReturn(List.of(dto1, dto2));

        // When
        List<TaskDto> result = taskService.getTasksByStatus(status, limit, offset).getContent();

        // Then
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(taskRepository).findTaskByStatus(status.name(), limit, offset);
        verify(taskMapper).toDtoList(List.of(entity1, entity2));
    }

    @Test
    @DisplayName("getTasksByStatus - Пустой результат при отсутствии задач с указанным статусом")
    void getTasksByStatusEmptyResultTest() {
        // Given
        Status status = Status.IN_PROGRESS;
        when(taskRepository.findTaskByStatus(status.name(), 20, 0))
                .thenReturn(List.of());
        when(taskMapper.toDtoList(List.of())).thenReturn(List.of());

        // When
        List<TaskDto> result = taskService.getTasksByStatus(status, 20, 0).getContent();

        // Then
        assertTrue(result.isEmpty());
    }
}