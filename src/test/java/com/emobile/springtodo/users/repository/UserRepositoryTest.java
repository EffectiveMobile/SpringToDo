package com.emobile.springtodo.users.repository;

import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.dto.out.UserResponseDto;
import com.emobile.springtodo.users.model.User;
import com.emobile.springtodo.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {

    private final UserRepositoryImpl userRepository;

//    public UserRepositoryTest(UserRepositoryImpl userRepository) {
//        this.userRepository = userRepository;
//    }


    @Test
    void getAllUsersList() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void createUserAccount() {
        NewUserRequestDto newUserRequestDto = new NewUserRequestDto("aa11@mail.com", "123");

        User user = userRepository.createUserAccount(newUserRequestDto);

    //    UserResponseDto userResponseDto = UserMapper.toUserResponseDto(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
    }

    @Test
    void updateUserAccount() {
    }

    @Test
    void deleteUserAccount() {
    }
}