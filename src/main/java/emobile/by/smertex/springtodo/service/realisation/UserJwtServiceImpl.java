package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.database.entity.nosql.UserJwt;
import emobile.by.smertex.springtodo.service.interfaces.UserJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserJwtServiceImpl implements UserJwtService {

    private final RedisTemplate<String, UserJwt> redisTemplate;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    @Override
    public UserJwt findByJwt(String jwt) {
        ValueOperations<String, UserJwt> ops = redisTemplate.opsForValue();
        return ops.get(jwt);
    }

    @Override
    public UserJwt save(UserJwt userJwt) {
        ValueOperations<String, UserJwt> ops = redisTemplate.opsForValue();
        ops.set(userJwt.getJwt(), userJwt, jwtLifetime);
        return userJwt;
    }
}
