package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.entity.ToDoEntity;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.repository.ToDoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDoService {

    private final ToDoRepository repository;
    private final ToDoMapper mapper;

    public ToDoService(ToDoRepository repository, ToDoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @Transactional
    @CacheEvict(value = "postgres", allEntries = true)
    public ToDoDto create(ToDoDto dto){
        ToDoEntity entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }
    @Transactional
    @Cacheable(value = "postgres", key = "#id")
    public ToDoDto findById(Long id){
        ToDoEntity entity = repository.findById(id)
                .orElseThrow(()-> new ToDoNotFoundException("ToDo not found with id: "+ id));
        return mapper.toDto(entity);
    }
    @Transactional
    @Cacheable(value = "postgres")
    public List<ToDoDto> findAll(int limit, int offset){
        return repository.findAll(limit,offset).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    @CacheEvict(value = "postgres", allEntries = true)
    public ToDoDto update(Long id, ToDoDto dto){
        ToDoEntity entity = repository.findById(id)
                .orElseThrow(()-> new ToDoNotFoundException("ToDo not found with id: "+ id));
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCompleted(dto.getCompleted());
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }
    @Transactional
    @CacheEvict(value = "postgres", allEntries = true)
    public void delete(Long id){
        if(!repository.findById(id).isPresent()){
            throw new ToDoNotFoundException("ToDo not found with id: "+ id);
        }
        repository.deleteByID(id);
    }
}
