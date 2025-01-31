package com.emobile.springtodo.utils.mappers;

import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.utils.exception.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper {

    private UserMapper() {
        log.info("Someone tries to crate exemplar of UserMapper Class!");
        throw new BadRequestException("This class is utility for purposes only!");
    }

    public static User toUser(NewUserRequestDto newUserDto) {

        new User();
        return User
                .builder()
                .email(newUserDto.email())
                .password(newUserDto.password())
                .build();
    }

    public static UserResponseDto toUserResponseDto(User userFromDb) {

        return new UserResponseDto(userFromDb.getEmail());
    }
}