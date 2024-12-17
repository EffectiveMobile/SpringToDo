package com.emobile.springtodo.users.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(@Schema(description = "Email author/Почта автора")
                              String email) {
}
