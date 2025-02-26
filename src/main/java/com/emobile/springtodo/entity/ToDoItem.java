package com.emobile.springtodo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс, представляющий задачу (ToDoItem) в системе.
 * Содержит информацию о задаче, такую как идентификатор, название, описание, статус выполнения и дата создания.
 *
 * @author PavelOkhrimchuk
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToDoItem {

    /**
     * Уникальный идентификатор задачи.
     */
    private Long id;

    /**
     * Название задачи.
     */
    private String title;

    /**
     * Описание задачи.
     */
    private String description;

    /**
     * Статус выполнения задачи.
     */
    private boolean completed;

    /**
     * Дата и время создания задачи.
     */
    private LocalDateTime createdAt;

}
