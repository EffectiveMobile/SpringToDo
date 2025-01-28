package emobile.by.smertex.springtodo.config;

import emobile.by.smertex.springtodo.util.ApiPath;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi publicTaskApi() {
        return GroupedOpenApi.builder()
                .group("Tasks")
                .pathsToMatch(ApiPath.TASK_PATH + ApiPath.ALL_PATH)
                .build();
    }

    @Bean
    public GroupedOpenApi publicAuthApi() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch(ApiPath.AUTH_PATH)
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(
            @Value("${application-data.description}") String description,
            @Value("${application-data.version}") String version,
            @Value("${application-data.name}") String appName,
            @Value("${application-data.contact.name}") String contactName,
            @Value("${application-data.contact.vk}") String contactVk,
            @Value("${application-data.contact.email}") String contactEmail) {
        return new OpenAPI().info(new Info()
                .title(appName)
                .version(version)
                .description(description)
                .contact(new Contact()
                        .email(contactEmail)
                        .name(contactName)
                        .url(contactVk)));
    }

}
