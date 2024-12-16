package com.emobile.springtodo.users.service;

import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import java.util.List;

public interface UserService {

    List<UserResponseDto> getListOfUsersDto(Integer from, Integer size);

    UserResponseDto getUserById(Long userId);


    UserResponseDto createUserAccount(NewUserRequestDto newUserDto);

    UserResponseDto updateUserAccount(Long userId, NewUserRequestDto newUserDto);

    UserResponseDto deleteUserAccount(Long userId);
}
