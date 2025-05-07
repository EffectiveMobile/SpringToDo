package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.ToDoDto;
import com.emobile.springtodo.service.ToDoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todos")
public class ToDoController implements ToDoAPI {

    private final ToDoService service;

    public ToDoController(ToDoService service) {
        this.service = service;
    }

    @PostMapping
    public ToDoDto create(@Valid @RequestBody ToDoDto dto){
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public ToDoDto getById(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping
    public List<ToDoDto> getAll(@RequestParam(defaultValue = "10") int limit,
                                @RequestParam(defaultValue = "0") int offset){
        return service.findAll(limit, offset);
    }

    @PutMapping("/id")
    public ToDoDto update(@PathVariable Long id, @Valid @RequestBody ToDoDto dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/id")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
