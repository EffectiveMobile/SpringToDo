package com.emobile.springtodo.users.repository;

import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.model.User;
import java.util.List;

public interface UserRepository {

    List<User> getAllUsersList(Integer from, Integer size);

    User getUserById(Long userId);

    User createUserAccount(NewUserRequestDto newUserDto);

    User updateUserAccount(Long userId, NewUserRequestDto newUserDto);

    User deleteUserAccount(Long userId);
}
