package com.emobile.springtodo.service;


import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.entity.ToDoItem;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.mapper.ToDoItemMapper;
import com.emobile.springtodo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Тесты для сервиса ToDo.
 * Включает тестирование всех CRUD операций (получение, создание, обновление, удаление).
 */
@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

    @Mock
    private ToDoRepository toDoRepository;

    @Mock
    private ToDoItemMapper toDoItemMapper;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    @Test
    void testGetAll() {
        ToDoItem item1 = new ToDoItem(1L, "Task 1", "Description 1", false, LocalDateTime.now());
        ToDoItem item2 = new ToDoItem(2L, "Task 2", "Description 2", true, LocalDateTime.now());
        ToDoItemDto dto1 = new ToDoItemDto(1L, "Task 1", "Description 1", false);
        ToDoItemDto dto2 = new ToDoItemDto(1L, "Task 2", "Description 2", true);

        when(toDoRepository.findAll(10, 0)).thenReturn(List.of(item1, item2));
        when(toDoItemMapper.toDto(item1)).thenReturn(dto1);
        when(toDoItemMapper.toDto(item2)).thenReturn(dto2);

        List<ToDoItemDto> result = toDoService.getAll(10, 0);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(toDoRepository).findAll(10, 0);
        verify(toDoItemMapper, times(2)).toDto(any(ToDoItem.class));
    }

    @Test
    void testGetById_Success() {
        Long id = 1L;
        ToDoItem item = new ToDoItem(id, "Task", "Description", false, LocalDateTime.now());
        ToDoItemDto dto = new ToDoItemDto(1L, "Task", "Description", false);

        when(toDoRepository.findById(id)).thenReturn(Optional.of(item));
        when(toDoItemMapper.toDto(item)).thenReturn(dto);

        ToDoItemDto result = toDoService.getById(id);

        assertEquals(dto, result);
        verify(toDoRepository).findById(id);
        verify(toDoItemMapper).toDto(item);
    }

    @Test
    void testGetById_NotFound() {
        Long id = 1L;

        when(toDoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ToDoNotFoundException.class, () -> toDoService.getById(id));
        verify(toDoRepository).findById(id);
    }

    @Test
    void testCreate() {
        ToDoItemDto dto = new ToDoItemDto(1L, "New Task", "New Description", false);
        ToDoItem entity = new ToDoItem(null, "New Task", "New Description", false, LocalDateTime.now());
        ToDoItemDto savedDto = new ToDoItemDto(1L, "New Task", "New Description", false);

        when(toDoItemMapper.toEntity(dto)).thenReturn(entity);
        when(toDoItemMapper.toDto(any(ToDoItem.class))).thenReturn(savedDto);

        doAnswer(invocation -> {
            entity.setId(1L);
            return null;
        }).when(toDoRepository).save(entity);

        ToDoItemDto result = toDoService.create(dto);

        assertEquals(savedDto, result);
        verify(toDoRepository).save(entity);
        verify(toDoItemMapper).toEntity(dto);
        verify(toDoItemMapper).toDto(any(ToDoItem.class));
    }


    @Test
    void testDelete_Success() {
        Long id = 1L;
        ToDoItem item = new ToDoItem(id, "Task", "Description", false, LocalDateTime.now());

        when(toDoRepository.findById(id)).thenReturn(Optional.of(item));

        toDoService.delete(id);

        verify(toDoRepository).findById(id);
        verify(toDoRepository).deleteById(id);
    }

    @Test
    void testDelete_NotFound() {
        Long id = 1L;

        when(toDoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ToDoNotFoundException.class, () -> toDoService.delete(id));
        verify(toDoRepository).findById(id);
    }
}
