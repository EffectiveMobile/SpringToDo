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

@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoItemMapper toDoItemMapper;

    @Override
    public List<ToDoItemDto> getAll(int limit, int offset) {
        log.info("Fetching ToDo items with limit={} and offset={}", limit, offset);
        return toDoRepository.findAll(limit, offset)
                .stream()
                .map(toDoItemMapper::toDto)
                .collect(Collectors.toList());
    }

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

    @Transactional
    @Override
    public ToDoItemDto create(ToDoItemDto dto) {
        log.info("Creating new ToDo item: {}", dto);
        ToDoItem entity = toDoItemMapper.toEntity(dto);
        toDoRepository.save(entity);
        return toDoItemMapper.toDto(entity);
    }

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
