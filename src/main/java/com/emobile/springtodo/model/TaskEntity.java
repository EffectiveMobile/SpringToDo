package com.emobile.springtodo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskEntity {
    private Integer id;
    private String title;
    private String description;
    private Status status;
}
