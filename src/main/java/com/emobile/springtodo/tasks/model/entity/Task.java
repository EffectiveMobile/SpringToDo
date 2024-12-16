package com.emobile.springtodo.tasks.model.entity;

import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.users.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import com.emobile.springtodo.users.model.entity.Priority;

@Getter
@Setter
@Builder( toBuilder = true)
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private Long id;

    private LocalDateTime creation;

    private String description;

    private String header;

    private Priority priority;

    private Status status;

    private User assignee;

    private User author;
}
