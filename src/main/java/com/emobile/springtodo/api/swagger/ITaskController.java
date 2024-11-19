package com.emobile.springtodo.api.swagger;

import com.emobile.springtodo.api.request.TaskCreationRequest;
import com.emobile.springtodo.api.request.TaskUpdateRequest;
import com.emobile.springtodo.api.request.validation.Patterns;
import com.emobile.springtodo.core.entity.Task;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Task controller", description = "Контроллер для работы с заданием")
public interface ITaskController {

    @Operation(
            summary = "Получение всех заданий",
            description = "Получение списка всех заданий из БД.",
            parameters = {
                    @Parameter(
                            name = "limit",
                            in = ParameterIn.QUERY,
                            description = "Максимальное количество записей, возвращаемых в одном запросе",
                            example = "10",
                            schema = @Schema(
                                    type = "int",
                                    minimum = "1"
                            )
                    ),
                    @Parameter(
                            name = "offset",
                            in = ParameterIn.QUERY,
                            description = "Смещение для начала выборки данных.",
                            example = "0",
                            schema = @Schema(
                                    type = "int",
                                    minimum = "0"
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Список заданий получен",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "List of all tasks",
                                                    description = "Ответ с информацией в виде списка всех заданий",
                                                    value = """
                                                                [
                                                                     {
                                                                         "createdAt": "2024-11-15T21:05:57.412871",
                                                                         "updatedAt": "2024-11-15T21:05:57.412871",
                                                                         "version": 1,
                                                                         "taskID": "acd95199-5d37-4db1-bbb2-dead95fb963f",
                                                                         "taskTitle": "TaskTitle2",
                                                                         "taskDescription": "TaskDescription",
                                                                         "taskStatus": "INPROGRESS",
                                                                         "taskDate": "2024-11-12T21:00:00.000+00:00",
                                                                         "taskHours": 2.5,
                                                                         "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                                     },
                                                                     {
                                                                         "createdAt": "2024-11-15T21:05:58.188383",
                                                                         "updatedAt": "2024-11-15T21:05:58.188383",
                                                                         "version": 1,
                                                                         "taskID": "02826d59-6cf6-4778-9212-6177739d2c78",
                                                                         "taskTitle": "TaskTitle2",
                                                                         "taskDescription": "TaskDescription",
                                                                         "taskStatus": "INPROGRESS",
                                                                         "taskDate": "2024-11-12T21:00:00.000+00:00",
                                                                         "taskHours": 2.5,
                                                                         "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                                     }
                                                                ]
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "Запрашиваемый лимит или оффсет для списка заданий не прошли валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-limit",
                                                    description = "Невалидный limit",
                                                    value = """
                                                            {
                                                                 "status": 400,
                                                                 "timestamp": "2024-11-16T18:01:26.534803800Z",
                                                                 "error": "IllegalArgumentException",
                                                                 "message": "Limit must be greater than 0"
                                                             }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "invalid-offset",
                                                    description = "Невалидный offset",
                                                    value = """
                                                            {
                                                                  "status": 400,
                                                                  "timestamp": "2024-11-16T18:05:34.564370100Z",
                                                                  "error": "IllegalArgumentException",
                                                                  "message": "Offset cannot be negative"
                                                            }
                                                            """
                                            )
                                    }
                            )),
            }
    )
    ResponseEntity<List<Task>> getAll(@RequestParam(defaultValue = "10") int limit,
                                      @RequestParam(defaultValue = "0") int offset);

    @Operation(
            summary = "Получение задания по ID",
            description = "Получение информации о задании по уникальному ID",
            parameters = {
                    @Parameter(
                            name = "taskid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID задания",
                            required = true,
                            example = "0deb8393-bf1f-4e3c-905d-4830c60cc687",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Информация о задании получена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Task-by-ID",
                                                    description = "Ответ с информацией о задании",
                                                    value = """
                                                            {
                                                                  "createdAt": "2024-11-15T17:49:34.7376745",
                                                                  "updatedAt": "2024-11-15T17:49:34.7376745",
                                                                  "version": 1,
                                                                  "taskID": "0deb8393-bf1f-4e3c-905d-4830c60cc687",
                                                                  "taskTitle": "TaskTitle2",
                                                                  "taskDescription": "TaskDescription",
                                                                  "taskStatus": "INPROGRESS",
                                                                  "taskDate": "2024-11-13T00:00:00.000+00:00",
                                                                  "taskHours": 2.5,
                                                                  "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                            }
                                                            """
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Запрашиваемый ID задания не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-id",
                                                    description = "Невалидный ID",
                                                    value = """
                                                            [
                                                                 "taskID: Invalid task ID format"
                                                            ]
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "Задание по запрашиваемому ID не найдено",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "task-not-found",
                                                    description = "Задание не найдено",
                                                    value = """
                                                            {
                                                                 "status": 404,
                                                                 "timestamp": "2024-11-16T17:59:48.345854Z",
                                                                 "error": "TaskNotFoundException",
                                                                 "message": "Task with ID 0deb8393-bf1f-4e3c-905d-4830c60cc687 not found"
                                                             }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<?> getTask(@Pattern(regexp = Patterns.ID, message = "Invalid task ID format") @PathVariable("taskid") String taskID);

    @Operation(
            summary = "Получение заданий по ID категорий",
            description = "Получение информации о списке заданий по категории, к которой они принадлежат",
            parameters = {
                    @Parameter(
                            name = "categoryid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID категории",
                            required = true,
                            example = "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Информация о списке заданий получена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Task-by-categoryID",
                                                    description = "Ответ с информацией о задании",
                                                    value = """
                                                            [
                                                                 {
                                                                     "createdAt": "2024-11-15T17:49:34.737675",
                                                                     "updatedAt": "2024-11-15T17:49:34.737675",
                                                                     "version": 1,
                                                                     "taskID": "0deb8393-bf1f-4e3c-905d-4830c60cc687",
                                                                     "taskTitle": "TaskTitle2",
                                                                     "taskDescription": "TaskDescription",
                                                                     "taskStatus": "INPROGRESS",
                                                                     "taskDate": "2024-11-12T21:00:00.000+00:00",
                                                                     "taskHours": 2.5,
                                                                     "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                                 },
                                                                 {
                                                                     "createdAt": "2024-11-15T17:50:27.092995",
                                                                     "updatedAt": "2024-11-15T17:50:27.092995",
                                                                     "version": 1,
                                                                     "taskID": "76a21b11-6c4a-4a69-92cc-2cf559508cf1",
                                                                     "taskTitle": "TaskTitle2",
                                                                     "taskDescription": "TaskDescription",
                                                                     "taskStatus": "INPROGRESS",
                                                                     "taskDate": "2024-11-12T21:00:00.000+00:00",
                                                                     "taskHours": 2.5,
                                                                     "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                                 }
                                                            ]
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
                            description = "По переданному параметру ID категории не найдена сама категория или не найдено заданий",
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
                                                                 "timestamp": "2024-11-16T18:19:24.942152600Z",
                                                                 "error": "CategoryNotFoundException",
                                                                 "message": "Category with ID d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a5 not found"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "task-list-not-found",
                                                    description = "Задания не найдены",
                                                    value = """
                                                            {
                                                                 "status": 404,
                                                                 "timestamp": "2024-11-16T18:43:32.793564700Z",
                                                                 "error": "TaskNotFoundException",
                                                                 "message": "No tasks found with Category ID 81568ddf-c0b1-4c15-b26a-38284c1d7243"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<?> getAllTasksByCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format") @PathVariable("categoryid") String categoryID);

    @Operation(
            summary = "Получение заданий по статусу",
            description = "Получение информации о списке заданий по статусу, к которой они принадлежат",
            parameters = {
                    @Parameter(
                            name = "status",
                            in = ParameterIn.PATH,
                            description = "Статус задания",
                            required = true,
                            example = "INPROGRESS",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.STATUS
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Информация о списке заданий получена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Task.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Task-by-status",
                                                    description = "Ответ с информацией о задании",
                                                    value = """
                                                            [
                                                                  {
                                                                      "createdAt": "2024-11-15T21:05:53.007719",
                                                                      "updatedAt": "2024-11-15T21:05:53.007719",
                                                                      "version": 1,
                                                                      "taskID": "117a5cea-006b-48c2-be91-760d087e0fea",
                                                                      "taskTitle": "TaskTitle2",
                                                                      "taskDescription": "TaskDescription",
                                                                      "taskStatus": "INPROGRESS",
                                                                      "taskDate": "2024-11-12T21:00:00.000+00:00",
                                                                      "taskHours": 2.5,
                                                                      "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                                  },
                                                                  {
                                                                      "createdAt": "2024-11-15T21:05:54.271397",
                                                                      "updatedAt": "2024-11-15T21:05:54.271397",
                                                                      "version": 1,
                                                                      "taskID": "e7f25100-7ff3-4132-9508-1c7419b83d76",
                                                                      "taskTitle": "TaskTitle2",
                                                                      "taskDescription": "TaskDescription",
                                                                      "taskStatus": "INPROGRESS",
                                                                      "taskDate": "2024-11-12T21:00:00.000+00:00",
                                                                      "taskHours": 2.5,
                                                                      "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                                  }
                                                            ]
                                                            """
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Запрашиваемый статус не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-status",
                                                    description = "Невалидный status",
                                                    value = """
                                                            [
                                                                  "status: Status must be one of: TODO, INPROGRESS, COMPLETE, UNCOMPLETED, DELAYED"
                                                            ]
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "По переданному параметру status не найдено заданий",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "task-list-not-found",
                                                    description = "Задания не найдены",
                                                    value = """
                                                            {
                                                                  "status": 404,
                                                                  "timestamp": "2024-11-16T19:59:09.593284700Z",
                                                                  "error": "TaskNotFoundException",
                                                                  "message": "No tasks found with Status COMPLETE"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<?> getAllTasksByStatus(@Pattern(regexp = Patterns.STATUS,
     message = "Status must be one of: TODO, INPROGRESS, COMPLETE, UNCOMPLETED, DELAYED")
                                          @PathVariable("status") String status);

    @Operation(
            summary = "Создание нового задания",
            description = "Создаёт новое задание на основе данных, переданных в запросе",
            responses = {
                    @ApiResponse(
                            description = "Задание создано",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Запрос на создание задания не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "validation-error",
                                                    description = "Ошибка валидации полей названия, описания, статуса, назначенных часов и категории задания",
                                                    value = """
                                                            {
                                                                "taskDescription": "Description is minimum 2 symbols and maximum 128 symbols.",
                                                                "taskHours": "Task hours cannot exceed 24",
                                                                "taskTitle": "Title is minimum 2 symbols and maximum 64 symbols.",
                                                                "taskStatus": "Status must be one of: TODO, INPROGRESS, COMPLETE, UNCOMPLETED, DELAYED",
                                                                "taskCategory": "Invalid UUID format for category."
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "date-error",
                                                    description = "Ошибка в формате передаваемой даты",
                                                    value = """
                                                            {
                                                                "taskDate": "Invalid date format. Expected format: yyyy-MM-dd"
                                                            }
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                    description = "По переданному параметру ID категории не найдено",
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
                                                                 "timestamp": "2024-11-16T18:19:24.942152600Z",
                                                                 "error": "CategoryNotFoundException",
                                                                 "message": "Category with ID d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a5 not found"
                                                            }
                                                            """
                                    )
                            }
                    ))

            },
            requestBody = @RequestBody(
                    description = "Данные для создания задания",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskCreationRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "task-creation-request",
                                            description = "Шаблон запроса на создание задания",
                                            value = """
                                                    {
                                                         "taskTitle": "TaskTitle2",
                                                         "taskDescription": "TaskDescription",
                                                         "taskStatus": "INPROGRESS",
                                                         "taskDate": "2024-11-13",
                                                         "taskHours": "2.5",
                                                         "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    ResponseEntity<?> addTask(@Valid @RequestBody TaskCreationRequest request);

    @Operation(
            summary = "Изменение задания",
            description = "Изменяет уже существующее задание на основе данных, переданных в запросе",
            parameters = {
                    @Parameter(
                            name = "taskid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID задания",
                            required = true,
                            example = "3d9dbe07-96b8-44c3-9b35-3869ac18649f",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Задание изменено",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Запрос на изменение задания не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-id",
                                                    description = "Невалидный ID",
                                                    value = """
                                                            [
                                                                "taskID: Invalid task ID format"
                                                            ]
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "validation-error",
                                                    description = "Ошибка валидации полей названия, описания, статуса и назначенных часов задания",
                                                    value = """
                                                            {
                                                                "taskDescription": "Description is minimum 2 symbols and maximum 128 symbols.",
                                                                "taskHours": "Task hours cannot exceed 24",
                                                                "taskTitle": "Title is minimum 2 symbols and maximum 64 symbols.",
                                                                "taskStatus": "Status must be one of: TODO, INPROGRESS, COMPLETE, UNCOMPLETED, DELAYED",
                                                                "taskCategory": "d99ee5a1-ea23-41d5-8ef2-a4dcc6fb49a4"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "date-error",
                                                    description = "Ошибка в формате передаваемой даты",
                                                    value = """
                                                            {
                                                                "taskDate": "Invalid date format. Expected format: yyyy-MM-dd"
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
                            description = "По переданному параметру ID задание или категория не найдены",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "task-not-found",
                                                    description = "Задание не найдено",
                                                    value = """
                                                            {
                                                                 "status": 404,
                                                                 "timestamp": "2024-11-16T17:59:48.345854Z",
                                                                 "error": "TaskNotFoundException",
                                                                 "message": "Task with ID 3d9dbe07-96b8-44c3-9b35-3869ac18649c not found"
                                                             }
                                                            """
                                            ),
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
                            ))
            },
            requestBody = @RequestBody(
                    description = "Данные для изменения задания",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskUpdateRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "task-update-request",
                                            description = "Шаблон запроса на изменение задания",
                                            value = """
                                                    {
                                                        "taskTitle": "UpdatedTitle",
                                                        "taskDescription": "UpdatedTaskDescription",
                                                        "taskStatus": "DELAYED",
                                                        "taskDate": "2024-11-14",
                                                        "taskHours": "4",
                                                        "taskCategory": "5a74eb25-aa8d-49d2-85ed-3b79f9a5b878"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    ResponseEntity<?> updateTask(@Pattern(regexp = Patterns.ID, message = "Invalid task ID format") @PathVariable("taskid") String taskID,
                                 @Valid @RequestBody TaskUpdateRequest request);

    @Operation(
            summary = "Удаление задания",
            description = "Удаляет существующее задание по ID",
            parameters = {
                    @Parameter(
                            name = "taskid",
                            in = ParameterIn.PATH,
                            description = "Уникальный ID задания",
                            required = true,
                            example = "8731ced0-31df-4084-8b8b-dba4a41dec96",
                            schema = @Schema(
                                    type = "string",
                                    pattern = Patterns.ID
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Задание удалено",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Запрашиваемый ID задания не прошёл валидацию",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "invalid-id",
                                                    description = "Невалидный ID",
                                                    value = """
                                                            [
                                                                "taskID: Invalid task ID format"
                                                            ]
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(
                            description = "По переданному параметру ID задание не найдено",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "task-not-found",
                                                    description = "Задание не найдено",
                                                    value = """
                                                            {
                                                                 "status": 404,
                                                                 "timestamp": "2024-11-17T02:33:58.455282900Z",
                                                                 "error": "TaskNotFoundException",
                                                                 "message": "Task with ID 8731ced0-31df-4084-8b8b-dba4a41dec95 not found"
                                                             }
                                                            """
                                            )
                                    }
                            )
                    )

            }
    )
    ResponseEntity<?> deleteTask(@Pattern(regexp = Patterns.ID, message = "Invalid task ID format") @PathVariable("taskid") String taskID);
}
