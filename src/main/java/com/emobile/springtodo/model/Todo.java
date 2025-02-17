package com.emobile.springtodo.model;

import lombok.*;

import java.io.Serializable;

/**
 * Модель задачи.
 *
 * @author Мельников Никита
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Todo implements Serializable {
    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
}
