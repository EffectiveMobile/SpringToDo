package com.emobile.springtodo.api.request;

import com.emobile.springtodo.api.request.validation.Patterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Схема запроса на изменение категории")
public record CategoryUpdateRequest(

        @Schema(
                description = "Название категории",
                example = "Work",
                minLength = 2,
                maxLength = 64
        )
        @Size(min = 2, max = 64, message = "Title is minimum 2 symbols and maximum 64 symbols.")
        String categoryTitle,

        @Schema(
                description = "Hex-код цвета категории",
                example = "#95C8F3",
                minLength = 7,
                maxLength = 7
        )
        @Size(min = 7, max = 7, message = "Hex should be 7 symbols (including #).")
        @Pattern(regexp = Patterns.COLOUR,
                message = "Invalid hex format for colour.")
        String categoryColour) {
}
