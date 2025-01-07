package emobile.by.smertex.springtodo.service;

import emobile.by.smertex.springtodo.annotation.IT;
import emobile.by.smertex.springtodo.database.entity.realisation.User;
import emobile.by.smertex.springtodo.service.realisation.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@IT
@RequiredArgsConstructor
public class UserServiceImplIT {

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private final UserServiceImpl userServiceImpl;

    @Test
    void findUserByEmail(){
        Optional<User> user = userServiceImpl.findByEmail(ADMIN_EMAIL_TEST);
        assertFalse(user.isEmpty());
        user.ifPresent(el -> assertEquals(el.getEmail(), ADMIN_EMAIL_TEST));
    }

}
