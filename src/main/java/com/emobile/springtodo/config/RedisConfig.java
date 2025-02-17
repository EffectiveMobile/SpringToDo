package com.emobile.springtodo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Конфигурация Redis для кэширования данных.
 * Этот класс настраивает RedisTemplate и RedisCacheConfiguration для работы с Redis.
 *
 * @author Мельников Никита
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Настройка RedisTemplate для сериализации ключей и значений.
     * Используется JSON-сериализация для значений и строковая сериализация для ключей.
     *
     * @param connectionFactory фабрика подключения к Redis
     * @return настроенный RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    /**
     * Настройка конфигурации кэша Redis.
     * Устанавливается время жизни записей в кэше (TTL) и отключается кэширование null-значений.
     *
     * @return настроенная конфигурация RedisCacheConfiguration
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();
    }
}
