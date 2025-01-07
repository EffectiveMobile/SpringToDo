package emobile.by.smertex.springtodo.config;

import emobile.by.smertex.springtodo.database.entity.nosql.UserJwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(@Value("${redis.host}") String redisHost,
                                                             @Value("${redis.port}") int redisPort) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        return factory;
    }

    @Bean
    public RedisTemplate<String, UserJwt> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, UserJwt> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }
}
