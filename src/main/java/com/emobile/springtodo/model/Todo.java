package com.emobile.springtodo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Todo {
    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
}
