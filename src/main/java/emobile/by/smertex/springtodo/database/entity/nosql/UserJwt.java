package emobile.by.smertex.springtodo.database.entity.nosql;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("User")
public class UserJwt implements Serializable {

    private String jwt;

    private String email;

    private List<Role> roles;
}
