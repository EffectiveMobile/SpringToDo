package com.emobile.springtodo.users.dto.in;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserAccount(@NotBlank
                                String password) {
}
