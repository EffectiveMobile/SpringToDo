package com.emobile.springtodo.users.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewUserRequestDto(@Email
                                @NotBlank
                                String email,

                                @NotBlank
                                String password
) {

}

