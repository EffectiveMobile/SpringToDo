package com.emobile.springtodo.users.service;

import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import java.util.List;

public interface UserService {

    List<UserResponseDto> gettListOfUsersDto(Integer from, Integer size);

    UserResponseDto getUserDtoById(Long userId);


    UserResponseDto createUserAccount(NewUserRequestDto newUserDto);

    UserResponseDto updateUserAccount(Long userId, NewUserRequestDto newUserDto);

    UserResponseDto deleteUserAccount(Long userId);
}
