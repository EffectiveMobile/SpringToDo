package com.emobile.springtodo.tasks.dto.out;

import com.emobile.springtodo.tasks.model.entity.Priority;
import com.emobile.springtodo.tasks.model.entity.Status;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import com.emobile.springtodo.users.model.User;
import java.time.LocalDateTime;

public record TaskResponseDto(Long id,

                              LocalDateTime creation,

                              String description,

                              String header,

                              Priority priority,

                              Status status,

                              UserResponseDto assignee,

                              UserResponseDto author) {
}
