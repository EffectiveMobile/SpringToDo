package com.emobile.springtodo.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "todos")
public class Todo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TodoStatus status;
}
