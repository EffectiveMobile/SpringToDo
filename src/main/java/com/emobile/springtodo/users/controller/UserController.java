package com.emobile.springtodo.users.controller;

import com.emobile.springtodo.users.dto.in.UpdateUserAccount;
import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import com.emobile.springtodo.users.service.UserService;
import com.emobile.springtodo.utils.Create;
import com.emobile.springtodo.utils.Update;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получение всех аккаунтов пользователей (USER)",
            description = "Позволяет получить аккаунты из БД"
    )
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getListOfUsersDto(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {

        return userService.getListOfUsersDto(from, size);
    }

    @Operation(
            summary = "Получение аккаунта пользователя (USER) по id (User)",
            description = "Позволяет получить аккаунт пользователя"
    )
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserDtoById(@Positive @PathVariable(name = "userId") Long userId) {

        return userService.getUserById(userId);
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя"
    )
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUserAccount(@Validated(Create.class) @RequestBody NewUserRequestDto newUserDto) {

        return userService.createUserAccount(newUserDto);
    }

    @Operation(
            summary = "Редактирование пользователя (USER) по id (User)",
            description = "Позволяет редактирование пользователя"
    )
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUserAccount(@Positive @PathVariable(name = "userId") Long userId,
                                             @Validated(Update.class) @RequestBody UpdateUserAccount updateUserDto) {

        return userService.updateUserAccount(userId, updateUserDto);
    }

    @Operation(
            summary = "Удаление пользователя (USER) по id (User)",
            description = "Позволяет удалить пользователя"
    )
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserResponseDto deleteUserAccount(@Positive @PathVariable(name = "userId") Long userId) {

        return userService.deleteUserAccount(userId);
    }
}
