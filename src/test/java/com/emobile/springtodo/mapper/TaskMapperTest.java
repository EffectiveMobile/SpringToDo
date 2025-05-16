package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.Status;
import com.emobile.springtodo.model.TaskEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class TaskMapperTest {

    @Autowired
    private TaskMapper taskMapper;


    @Test
    @DisplayName("toDto - Корректное преобразование TaskEntity в TaskDto")
    void mapEntityToDtoTest() {
        //given
        TaskEntity entity = new TaskEntity();
        entity.setId(1);
        entity.setTitle("Test");
        entity.setDescription("Test");
        entity.setStatus(Status.PENDING);

        //when
        TaskDto dto = taskMapper.toDto(entity);

        //then
        assertNotNull(dto);
        assertEquals(dto.getTitle(),entity.getTitle());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getStatus(),entity.getStatus().toString());
    }

    @Test
    @DisplayName("toEntity - Корректное преобразование TaskDto в TaskEntity")
    void mapDtoToEntityTest() {
        //given
        TaskDto dto = new TaskDto();
        dto.setTitle("Test");
        dto.setDescription("Test Description");
        dto.setStatus("IN_PROGRESS");

        //when
        TaskEntity entity = taskMapper.toEntity(dto);

        //then
        assertNotNull(entity);
        assertEquals(entity.getTitle(),dto.getTitle());
        assertEquals(entity.getDescription(),dto.getDescription());
        assertEquals(entity.getStatus().toString(),dto.getStatus());
    }

    @Test
    @DisplayName("toDtoList - Корректное преобразование списка TaskEntity в список TaskDto")
    void mapEntityListToDtoList() {
        //given
        TaskEntity entity1 = new TaskEntity();
        entity1.setId(1);
        entity1.setTitle("Task 1");
        entity1.setDescription("Desc 1");
        entity1.setStatus(Status.PENDING);

        TaskEntity entity2 = new TaskEntity();
        entity2.setId(2);
        entity2.setTitle("Task 2");
        entity2.setDescription("Desc 2");
        entity2.setStatus(Status.COMPLETED);

        List<TaskEntity> entities = List.of(entity1, entity2);

        //when
        List<TaskDto> dtos = taskMapper.toDtoList(entities);

        //then
        assertNotNull(dtos);
        assertEquals(dtos.get(0).getTitle(),entity1.getTitle());
        assertEquals(dtos.get(1).getTitle(),entity2.getTitle());
    }

    @Test
    @DisplayName("toDto/toEntity/toDtoList - Обработка null входных значений")
    void nullInputsTest() {
        //given
        TaskDto nullDto = taskMapper.toDto(null);
        TaskEntity nullEntity = taskMapper.toEntity(null);

        //when
        List<TaskDto> nullList = taskMapper.toDtoList(null);

        //then
        assertNull(nullDto);
        assertNull(nullEntity);
        assertNull(nullList);
    }

    @Test
    @DisplayName("toDto - Обработка TaskEntity с null полями")
    void nullFieldsTest() {
        //given
        TaskEntity entity = new TaskEntity();
        entity.setId(1);

        //when
        TaskDto dto = taskMapper.toDto(entity);

        //then
        assertNotNull(dto);
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getStatus());
    }

    @Test
    @DisplayName("toEntity - Обработка TaskDto с null полями")
    void nullFieldsTEst() {
        //given
        TaskDto dto = new TaskDto();

        //when
        TaskEntity entity = taskMapper.toEntity(dto);

        //then
        assertNotNull(entity);
        assertNull(entity.getTitle());
        assertNull(entity.getDescription());
        assertNull(entity.getStatus());
    }
}