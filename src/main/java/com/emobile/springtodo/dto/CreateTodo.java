package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTodo {

    private String title;

    private String description;

    private TodoStatus status;
}
