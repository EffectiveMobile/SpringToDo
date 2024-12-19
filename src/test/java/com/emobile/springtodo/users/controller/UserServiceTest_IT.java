package com.emobile.springtodo.users.controller;


import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@Testcontainers
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class UserServiceTest_IT {

}
