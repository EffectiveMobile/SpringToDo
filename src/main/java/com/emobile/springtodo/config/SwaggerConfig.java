package com.emobile.springtodo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger для документирования API.
 * Этот класс настраивает OpenAPI-документацию, которая используется для генерации интерфейса Swagger UI.
 *
 * @author Мельников Никита
 */
@Configuration
public class SwaggerConfig {

    /**
     * Создает и настраивает объект OpenAPI для документирования API.
     *
     * @return настроенный объект OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo API")
                        .version("1.0")
                        .description("API для управления задачами (TODO-приложение)")
                        .contact(new io.swagger.v3.oas.models.info.Contact()));
    }
}
