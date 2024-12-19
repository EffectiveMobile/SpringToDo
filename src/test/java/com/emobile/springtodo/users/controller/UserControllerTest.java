package com.emobile.springtodo.users.controller;

import com.emobile.springtodo.users.dto.in.NewUserRequestDto;
import com.emobile.springtodo.users.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    private final ObjectMapper objectMapper;

    @Autowired
    private final MockMvc mockMvc;

    @MockBean
    private final UserServiceImpl userService;

    private final NewUserRequestDto user1 = new NewUserRequestDto("test@test.com", "123");

    private final NewUserRequestDto user2 = new NewUserRequestDto("new@test.ru", "321");

    @Autowired
    public UserControllerTest(ObjectMapper objectMapper, MockMvc mockMvc, UserServiceImpl userService) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
        this.userService = userService;
    }

    @Test
    void getListOfUsersDto() throws Exception {

        userService.createUserAccount(user1);
        userService.createUserAccount(user2);

        mockMvc.perform(get("/users")).andExpect(status().isOk());

    }

    @Test
    void getUserDtoById() throws Exception {

        mockMvc.perform(
                        get("/users/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void createUserAccount() {
    }

    @Test
    void updateUserAccount() {
    }

    @Test
    void deleteUserAccount() {
    }
}