package com.emobile.springtodo.users.service;

import com.emobile.springtodo.users.dto.in.UpdateUserAccount;
import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import com.emobile.springtodo.users.repository.UserRepository;
import com.emobile.springtodo.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getListOfUsersDto(Integer from, Integer size) {

        return userRepository.getAllUsersList(from, size)
                                                        .stream()
                                                        .map(UserMapper::toUserResponseDto)
                                                        .toList();
    }

    @Override
    public UserResponseDto getUserById(Long userId) {

        return UserMapper.toUserResponseDto(userRepository.getUserById(userId));
    }

    @Override
    public UserResponseDto createUserAccount(NewUserRequestDto newUserDto) {

        return UserMapper.toUserResponseDto(userRepository.createUserAccount(newUserDto));
    }

    @Override
    public UserResponseDto updateUserAccount(Long userId, UpdateUserAccount updateUserDto) {

        return UserMapper.toUserResponseDto(userRepository.updateUserAccount(userId, updateUserDto));
    }

    @Override
    public UserResponseDto deleteUserAccount(Long userId) {

        return UserMapper.toUserResponseDto(userRepository.deleteUserAccount(userId));
    }
}
