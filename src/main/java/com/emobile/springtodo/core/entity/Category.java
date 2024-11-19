package com.emobile.springtodo.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Schema(description = "Модель категории, связанной с задачами пользователя.")
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Category extends BaseEntity {


    @Schema(description = "Уникальный идентификатор категории", example = "2cb7304c-0116-464c-94e5-e4ea8b799460")
    private UUID categoryID;

    @Schema(description = "Название категории", example = "Work")
    private String categoryTitle;

    @Schema(description = "Цвет категории в формате HEX", example = "#CBDFBC")
    private String categoryColour;

    @Schema(description = "Уникальный идентификатор пользователя, которому принадлежит категория", example = "e1a2b3c4-d5e6-f7a8-9b0c-d1e2f3a4b5c6")
    private UUID accountID;

}
