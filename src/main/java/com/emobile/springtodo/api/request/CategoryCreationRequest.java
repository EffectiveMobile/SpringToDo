package com.emobile.springtodo.api.request;

import com.emobile.springtodo.api.request.validation.Patterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Схема запроса на создание категории")
public record CategoryCreationRequest(

        @Schema(
                description = "Название категории",
                example = "Work",
                minLength = 2,
                maxLength = 64,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Title is required.")
        @Size(min = 2, max = 64, message = "Title is minimum 2 symbols and maximum 64 symbols.")
        String categoryTitle,

        @Schema(
                description = "Hex-код цвета категории",
                example = "#95C8F3",
                minLength = 7,
                maxLength = 7,
                pattern = Patterns.COLOUR
        )
        @Size(min = 7, max = 7, message = "Hex should be 7 symbols.")
        @Pattern(regexp = Patterns.COLOUR,
                message = "Invalid hex format for colour (including #).")
        String categoryColour,

        @Schema(
                description = "ID аккаунта, создающего категорию",
                example = "83aeca1f-9d50-41e1-9a2e-529c60437370",
                pattern = Patterns.ID,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "UUID of account is required.")
        @Pattern(regexp = Patterns.ID,
                message = "Invalid UUID format for account.")
        String accountID) {
}
