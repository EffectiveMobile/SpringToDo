package com.emobile.springtodo.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI для документации API.
 * Настроена информация о версии и описании API.
 *
 * @author PavelOkhrimchuk
 */
@Configuration
public class OpenApiConfig {

    /**
     * Создает объект OpenAPI с информацией о приложении.
     *
     * @return OpenAPI объект с настроенной информацией.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring ToDo API")
                        .version("1.0")
                        .description("API для управления задачами ToDo"));
    }
}
