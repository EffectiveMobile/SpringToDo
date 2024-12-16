package com.emobile.springtodo.users.repository;

import com.emobile.springtodo.users.model.User;
import java.util.List;

public interface UserRepository {

    List<User> getAllUsersList(Integer from, Integer size);




}
