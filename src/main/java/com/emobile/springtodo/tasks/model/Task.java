package com.emobile.springtodo.tasks.model;

import com.emobile.springtodo.tasks.model.entity.Priority;
import com.emobile.springtodo.tasks.model.entity.Status;
import com.emobile.springtodo.users.model.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

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
