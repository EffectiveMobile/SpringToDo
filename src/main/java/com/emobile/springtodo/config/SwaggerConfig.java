package com.emobile.springtodo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI integration in the To Do Spring.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "OpenApi specification - To Do Spring",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        }
)
@Configuration
public class SwaggerConfig {
}
