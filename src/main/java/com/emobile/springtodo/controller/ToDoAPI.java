package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.ToDoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(name = "ToDoAPI", description = "API for managing TODO items")
public interface ToDoAPI {

    @Operation(summary = "Create a new TODO item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO created"),
            @ApiResponse(responseCode = "404", description = "invalid input")
    })
    ToDoDto create(ToDoDto dto);

    @Operation(summary = "Get TODO item by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO found"),
            @ApiResponse(responseCode = "404", description = "TODO not found")
    })
    ToDoDto getById(Long id);

    @Operation(summary = "Get all TODO items with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of TODOs")
    })
    List<ToDoDto> getAll(int limit, int offset);

    @Operation(summary = "Update TODO item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO updated"),
            @ApiResponse(responseCode = "404", description = "TODO not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    ToDoDto update(Long id, ToDoDto dto);

    @Operation(summary = "Delete TODO item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO deleted"),
            @ApiResponse(responseCode = "404", description = "TODO not found")
    })
    void delete(Long id);
}
