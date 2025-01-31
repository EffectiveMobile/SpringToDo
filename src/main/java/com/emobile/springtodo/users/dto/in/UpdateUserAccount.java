package com.emobile.springtodo.users.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserAccount(@NotBlank
                                @Schema(description = "Password user/Пароль юзера")
                                String password) {
}
