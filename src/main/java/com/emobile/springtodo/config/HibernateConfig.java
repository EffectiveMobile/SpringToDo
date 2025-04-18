package com.emobile.springtodo.config;

import com.emobile.springtodo.model.Todo;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Hibernate для работы с данными.
 * Этот класс настраивает SessionFactory для работы с Hibernate.
 *
 * @author Мельников Никита
 */
@Configuration
public class HibernateConfig {

    /**
     * Настройка SessionFactory для работы Hibernate.
     * Здесь настраиваются основные свойства для корректной работы Hibernate в рамках приложения.
     *
     * @return настроенный SessionFactory
     */
    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration.addAnnotatedClass(Todo.class);
        configuration.setProperty("hibernate.connection.url", System.getenv("SPRING_DATASOURCE_URL"));
        configuration.setProperty("hibernate.connection.username", System.getenv("SPRING_DATASOURCE_USERNAME"));
        configuration.setProperty("hibernate.connection.password", System.getenv("SPRING_DATASOURCE_PASSWORD"));
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "none");
        configuration.setProperty("hibernate.show_sql", "true");

        return configuration.buildSessionFactory();
    }
}
