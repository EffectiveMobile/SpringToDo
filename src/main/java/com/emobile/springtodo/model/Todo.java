package com.emobile.springtodo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Todo {
    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
}
