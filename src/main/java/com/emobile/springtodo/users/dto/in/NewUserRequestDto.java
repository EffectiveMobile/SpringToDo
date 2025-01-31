package com.emobile.springtodo.users.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewUserRequestDto(@Email
                                @NotBlank
                                @Schema(description = "Email author/Почта автора")
                                String email,

                                @NotBlank
                                @Schema(description = "Password user/Пароль юзера")
                                String password
) {

}

