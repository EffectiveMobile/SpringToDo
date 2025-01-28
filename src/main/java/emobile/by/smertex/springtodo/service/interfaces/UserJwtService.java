package emobile.by.smertex.springtodo.service.interfaces;

import emobile.by.smertex.springtodo.database.entity.nosql.UserJwt;

public interface UserJwtService {
    UserJwt findByJwt(String jwt);

    UserJwt save(UserJwt userJwt);
}
