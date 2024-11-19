package com.emobile.springtodo.api.swagger;

import com.emobile.springtodo.api.request.CategoryCreationRequest;
import com.emobile.springtodo.api.request.CategoryUpdateRequest;
import com.emobile.springtodo.api.request.validation.Patterns;
import com.emobile.springtodo.core.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Category controller", description = "Контроллер для работы с категорией задания")
public interface ICategoryController {

    @Operation(
            summary = "Получение всех категорий",
            description = "Получение списка всех категорий из БД.",
            responses = {
                    @ApiResponse(
                            description = "Список категорий получен",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Category.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "List of all categories",
                                                    description = "Ответ с информацией в виде списка всех категорий",
                                                    value = """
                                                                [
                                                                     {
                                                                         "createdAt": "2024-11-15T16:10:34.91526",
                                                                         "updatedAt": "2024-11-15T16:10:34.91526",
                                                                         "version": 1,
                                                                         "categoryID": "67531a89-298d-4038-bd60-9f9357143f9a",
                                                                         "categoryTitle": "Default",
                                                                         "categoryColour": "#95C8F3",
                                                                         "accountID": "83aeca1f-9d50-41e1-9a2e-529c60437370"
                                                                     },
                                                                     {
                                                                         "createdAt": "2024-11-15T16:10:37.427416",
                                                                         "updatedAt": "2024-11-15T16:10:37.427416",
                                                                         "version": 1,
                                                                         "categoryID": "81568ddf-c0b1-4c15-b26a-38284c1d7243",
                                                                         "categoryTitle": "Work",
                                                                         "categoryColour": "#95C8F3",
                                                                         "accountID": "83aeca1f-9d50-41e1-9a2e-529c60437370"
                                                                     }
                                                                ]
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<List<Category>> getAll();

    @Operation(
            summary = "Получение категории по ID",
            description = "Получение информации о категории по уникальному ID",
            parameters = {
                    @Parameter(
                            name = "categoryid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID категории",
                            required = true,
                            example = "67531a89-298d-4038-bd60-9f9357143f9a",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Информация о категории получена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Category.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Category-by-ID",
                                                    description = "Ответ с информацией о категории",
                                                    value = """
                                                            {
                                                                 "createdAt": "2024-11-15T16:10:34.91526",
                                                                 "updatedAt": "2024-11-15T16:10:34.91526",
                                                                 "version": 1,
                                                                 "categoryID": "67531a89-298d-4038-bd60-9f9357143f9a",
                                                                 "categoryTitle": "Default",
                                                                 "categoryColour": "#95C8F3",
                                                                 "accountID": "83aeca1f-9d50-41e1-9a2e-529c60437370"
                                                             }
                                                            """
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Запрашиваемый ID категории не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-id",
                                                    description = "Невалидный ID",
                                                    value = """
                                                            [
                                                                 "categoryID: Invalid category ID format"
                                                            ]
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "Категория по запрашиваемому ID не найдена",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "category-not-found",
                                                    description = "Категория не найдена",
                                                    value = """
                                                            {
                                                                  "status": 404,
                                                                  "timestamp": "2024-11-16T13:46:11.489875300Z",
                                                                  "error": "CategoryNotFoundException",
                                                                  "message": "Category with ID 167e2947-8cc1-4430-b3f6-68fd14565cfd not found"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<?> getCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format") @PathVariable("categoryid") String categoryID);

    @Operation(
            summary = "Получение категорий по ID аккаунта",
            description = "Получение информации о списке категорий по создавшему их аккаунту",
            parameters = {
                    @Parameter(
                            name = "accountid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID аккаунта",
                            required = true,
                            example = "83aeca1f-9d50-41e1-9a2e-529c60437370",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Информация о списке категорий получена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Category.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Category-by-accountID",
                                                    description = "Ответ с информацией о категории",
                                                    value = """
                                                            [
                                                                  {
                                                                       "createdAt": "2024-11-15T16:10:34.91526",
                                                                       "updatedAt": "2024-11-15T16:10:34.91526",
                                                                       "version": 1,
                                                                       "categoryID": "67531a89-298d-4038-bd60-9f9357143f9a",
                                                                       "categoryTitle": "Default",
                                                                       "categoryColour": "#95C8F3",
                                                                       "accountID": "83aeca1f-9d50-41e1-9a2e-529c60437370"
                                                                   },
                                                                  {
                                                                      "createdAt": "2024-11-15T16:10:37.427416",
                                                                      "updatedAt": "2024-11-15T16:10:37.427416",
                                                                      "version": 1,
                                                                      "categoryID": "81568ddf-c0b1-4c15-b26a-38284c1d7243",
                                                                      "categoryTitle": "Work",
                                                                      "categoryColour": "#95C8F3",
                                                                      "accountID": "83aeca1f-9d50-41e1-9a2e-529c60437370"
                                                                  }
                                                            ]
                                                            """
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Запрашиваемый ID аккаунта не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-id",
                                                    description = "Невалидный ID",
                                                    value = """
                                                            [
                                                                 "accountID: Invalid category ID format"
                                                            ]
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "По переданному параметру ID аккаунт не найден",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "account-not-found",
                                                    description = "Аккаунт не найден",
                                                    value = """
                                                            {
                                                                 "status": 404,
                                                                 "timestamp": "2024-11-16T14:08:02.770518Z",
                                                                 "error": "CategoryNotFoundException",
                                                                 "message": "No categories found with Account ID 83aeca1f-9d50-41e1-9a2e-529c60437371"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<?> getAllCategoriesByAccount(@Pattern(regexp = Patterns.ID, message = "Invalid account ID format") @PathVariable("accountid") String accountID);

    @Operation(
            summary = "Создание новой категории",
            description = "Создаёт новую категорию на основе данных, переданных в запросе",
            responses = {
                    @ApiResponse(
                            description = "Категория создана",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Запрос на создание категории не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "validation-error",
                                                    description = "Ошибка валидации полей ID аккаунта, цвета и названия категории",
                                                    value = """
                                                            {
                                                                 "accountID": "Invalid UUID format for account.",
                                                                 "categoryColour": "Hex should be 7 symbols.",
                                                                 "categoryTitle": "Title is minimum 2 symbols and maximum 64 symbols."
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )

            },
            requestBody = @RequestBody(
                    description = "Данные для создания категории",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryCreationRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "category-creation-request",
                                            description = "Шаблон запроса на создание категории",
                                            value = """
                                                    {
                                                    	"categoryTitle": "Work",
                                                        "categoryColour": "#95c8f3",
                                                        "accountID": "83aeca1f-9d50-41e1-9a2e-529c60437370"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    ResponseEntity<?> addCategory(@Valid @RequestBody CategoryCreationRequest request);

    @Operation(
            summary = "Изменение категории",
            description = "Изменяет уже существующую категорию на основе данных, переданных в запросе",
            parameters = {
                    @Parameter(
                            name = "categoryid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID категории",
                            required = true,
                            example = "67531a89-298d-4038-bd60-9f9357143f9a",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Категория изменена",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Запрос на изменение категории не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-id",
                                                    description = "Невалидный ID",
                                                    value = """
                                                            [
                                                                 "categoryID: Invalid category ID format"
                                                            ]
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "validation-error",
                                                    description = "Ошибка валидации полей цвета и названия категории",
                                                    value = """
                                                            {
                                                                "categoryColour": "Invalid hex format for colour.",
                                                                "categoryTitle": "Title is minimum 2 symbols and maximum 64 symbols."
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "null-error",
                                                    description = "Ошибка пустого запроса",
                                                    value = """
                                                                Request body cannot be empty
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "По переданному параметру ID категория не найдена",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "category-not-found",
                                                    description = "Категория не найдена",
                                                    value = """
                                                            {
                                                                "status": 404,
                                                                "timestamp": "2024-11-16T15:45:43.400476800Z",
                                                                "error": "CategoryNotFoundException",
                                                                "message": "Category with ID d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a5 not found"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )

            },
            requestBody = @RequestBody(
                    description = "Данные для изменения категории",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryUpdateRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "category-update-request",
                                            description = "Шаблон запроса на изменение категории",
                                            value = """
                                                    {
                                                         "categoryTitle": "Work",
                                                         "categoryColour": "#bbccdd"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    ResponseEntity<?> updateCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format") @PathVariable("categoryid") String categoryID,
                                     @Valid @RequestBody CategoryUpdateRequest request);

    @Operation(
            summary = "Удаление категории",
            description = "Удаляет существующую категорию по ID",
            parameters = {
                    @Parameter(
                            name = "categoryid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID категории",
                            required = true,
                            example = "67531a89-298d-4038-bd60-9f9357143f9a",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Категория удалена",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Запрашиваемый ID категории не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-id",
                                                    description = "Невалидный ID",
                                                    value = """
                                                            [
                                                                 "categoryID: Invalid category ID format"
                                                            ]
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "По переданному параметру ID категория не найдена",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "category-not-found",
                                                    description = "Категория не найдена",
                                                    value = """
                                                            {
                                                                "status": 404,
                                                                "timestamp": "2024-11-16T15:45:43.400476800Z",
                                                                "error": "CategoryNotFoundException",
                                                                "message": "Category with ID d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a5 not found"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )

            }
    )
    ResponseEntity<?> deleteCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format")
                                            @PathVariable("categoryid") String categoryID);
}
