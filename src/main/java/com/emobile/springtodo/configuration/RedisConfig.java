package com.emobile.springtodo.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Конфигурация Redis кеширования.
 * Включает создание и настройку CacheManager для работы с Redis в приложении.
 *
 * @author PavelOkhrimchuk
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Создает и настраивает CacheManager для работы с Redis.
     *
     * @param connectionFactory Фабрика подключения к Redis.
     * @return CacheManager, использующий Redis для кеширования.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }
}
