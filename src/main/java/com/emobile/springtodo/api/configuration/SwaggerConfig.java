package com.emobile.springtodo.api.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ToDo Service API",
                description = "API для создания, получения, редактирования и удаления планов.",
                version = "1.0",
                contact = @Contact(
                        name = "o11ezha"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Локально развёрнутое приложение"
                )
        }
)
public class SwaggerConfig {
}
