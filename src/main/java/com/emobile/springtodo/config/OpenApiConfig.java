package com.emobile.springtodo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(
        title = "TaskDto Management System",
        description = "TO-DO List",
        version = "1.0.0",
        contact = @Contact(
                name = "Sirik Vadim"
        )
))
class OpenApiConfig {
}
