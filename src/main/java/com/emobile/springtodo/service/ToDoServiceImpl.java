package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoItemDto;
import com.emobile.springtodo.entity.ToDoItem;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.mapper.ToDoItemMapper;
import com.emobile.springtodo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления задачами ToDo.
 * Включает методы для получения задач, создания, обновления и удаления задач с логированием.
 *
 * @author PavelOkhrimchuk
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;
    private final ToDoItemMapper toDoItemMapper;

    /**
     * Получить все задачи с пагинацией.
     *
     * @param limit  Количество задач для выборки.
     * @param offset Смещение для выборки.
     * @return Список задач в виде DTO.
     */
    @Override
    public List<ToDoItemDto> getAll(int limit, int offset) {
        log.info("Fetching ToDo items with limit={} and offset={}", limit, offset);
        return toDoRepository.findAll(limit, offset)
                .stream()
                .map(toDoItemMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить задачу по ID с кешированием.
     * @param id Идентификатор задачи.
     * @return DTO задачи.
     */
    @Cacheable(value = "todos", key = "#id")
    @Override
    public ToDoItemDto getById(Long id) {
        log.info("Fetching ToDo item with id={}", id);
        ToDoItem item = toDoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("ToDo item with id={} not found", id);
                    return new ToDoNotFoundException(id);
                });
        return toDoItemMapper.toDto(item);
    }

    /**
     * Создание новой задачи.
     * @param dto DTO задачи для создания.
     * @return Созданная задача в виде DTO.
     */
    @Transactional
    @Override
    public ToDoItemDto create(ToDoItemDto dto) {
        log.info("Creating new ToDo item: {}", dto);
        ToDoItem entity = toDoItemMapper.toEntity(dto);
        toDoRepository.save(entity);
        return toDoItemMapper.toDto(entity);
    }

    /**
     * Обновление задачи по ID с кешированием.
     * @param id Идентификатор задачи для обновления.
     * @param dto DTO с новыми данными задачи.
     * @return Обновленная задача в виде DTO.
     */
    @Transactional
    @CachePut(value = "todos", key = "#id")
    @Override
    public ToDoItemDto update(Long id, ToDoItemDto dto) {
        log.info("Updating ToDo item with id={}", id);
        ToDoItem item = toDoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update - ToDo item with id={} not found", id);
                    return new ToDoNotFoundException(id);
                });

        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setCompleted(dto.isCompleted());

        toDoRepository.update(item);
        return toDoItemMapper.toDto(item);
    }

    /**
     * Удаление задачи по ID с кешированием.
     * @param id Идентификатор задачи для удаления.
     */
    @Transactional
    @CacheEvict(value = "todos", key = "#id")
    @Override
    public void delete(Long id) {
        log.info("Deleting ToDo item with id={}", id);
        if (toDoRepository.findById(id).isEmpty()) {
            log.warn("Cannot delete - ToDo item with id={} not found", id);
            throw new ToDoNotFoundException(id);
        }
        toDoRepository.deleteById(id);
    }
}
