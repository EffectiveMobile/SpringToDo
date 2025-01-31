package com.emobile.springtodo.users.repository;

import com.emobile.springtodo.users.model.User;
import java.util.List;

public interface UserRepositoryDao {

    List<User> getAllUsersList(Integer from, Integer size);

    User getUserById(Long userId);

    User createUserAccount(User newUser);

    User updateUserAccount(Long userId, User updateUserDto);

    User deleteUserAccount(Long userId);
}
