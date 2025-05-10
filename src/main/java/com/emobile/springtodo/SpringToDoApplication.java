package com.emobile.springtodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения Spring Boot для ToDo.
 * Запускает приложение.
 *
 * @author PavelOkhrimchuk
 */
@SpringBootApplication
public class SpringToDoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringToDoApplication.class, args);
    }

}
