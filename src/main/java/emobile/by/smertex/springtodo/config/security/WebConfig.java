package emobile.by.smertex.springtodo.config.security;

import emobile.by.smertex.springtodo.util.ApiPath;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс, конфигурирующий CORS доступ к приложению из доменов, котоыре приведены ниже
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ApiPath.ALL_PATH)
                .allowedOrigins("https://editor.swagger.io/", "http://localhost:8080/", "http://localhost:8095/")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
