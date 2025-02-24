package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDoItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToDoRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ToDoRepositoryImpl toDoRepository;


    @Test
    void testSave() {
        ToDoItem toDoItem = new ToDoItem(null, "Title", "Description", false, LocalDateTime.now());
        String sql = "INSERT INTO todo_items (title, description, completed, created_at) VALUES (?,?,?,?) RETURNING id";
        Long generatedId = 1L;

        when(jdbcTemplate.queryForObject(eq(sql), eq(Long.class), any(), any(), any(), any()))
                .thenReturn(generatedId);

        toDoRepository.save(toDoItem);

        assertEquals(generatedId, toDoItem.getId());
        verify(jdbcTemplate).queryForObject(eq(sql), eq(Long.class), any(), any(), any(), any());
    }

    @Test
    void testUpdate() {
        ToDoItem toDoItem = new ToDoItem(1L, "Updated Title", "Updated Description", true, LocalDateTime.now());
        String sql = "UPDATE todo_items SET title = ?, description = ?, completed = ? WHERE id = ?";

        toDoRepository.update(toDoItem);

        verify(jdbcTemplate).update(eq(sql), any(), any(), any(), eq(toDoItem.getId()));
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        String sql = "DELETE FROM todo_items WHERE id = ?";

        toDoRepository.deleteById(id);

        verify(jdbcTemplate).update(eq(sql), eq(id));
    }
}
