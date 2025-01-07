package emobile.by.smertex.springtodo.service;

import emobile.by.smertex.springtodo.annotation.IT;
import emobile.by.smertex.springtodo.database.entity.realisation.enums.Role;
import emobile.by.smertex.springtodo.dto.security.JwtRequest;
import emobile.by.smertex.springtodo.service.exception.AuthException;
import emobile.by.smertex.springtodo.service.realisation.AuthServiceImpl;
import emobile.by.smertex.springtodo.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
public class AuthServiceImplIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final String PASSWORD_TEST = "qwertyui12345678";

    private final AuthServiceImpl authServiceImpl;

    private final JwtTokenUtils jwtTokenUtils;

    @Test
    @SuppressWarnings("all")
    void authenticationAdmin(){
        JwtRequest jwtRequest = JwtRequest.builder()
                .username(ADMIN_EMAIL_TEST)
                .password(PASSWORD_TEST)
                .build();

        String token = authServiceImpl.authentication(jwtRequest);

        assertEquals(jwtTokenUtils.getUsername(token), ADMIN_EMAIL_TEST);
        jwtTokenUtils.getRoles(token).contains(Role.ADMIN.getEditedRole());
    }

    @Test
    @SuppressWarnings("all")
    void authenticationUser(){
        JwtRequest jwtRequest = JwtRequest.builder()
                .username(USER_EMAIL_TEST)
                .password(PASSWORD_TEST)
                .build();

        String token = authServiceImpl.authentication(jwtRequest);

        assertEquals(jwtTokenUtils.getUsername(token), USER_EMAIL_TEST);
        jwtTokenUtils.getRoles(token).contains(Role.USER.getEditedRole());
    }

    @Test
    void incorrectPasswordOrLoginTest(){
        JwtRequest jwtRequest = JwtRequest.builder()
                .username(ADMIN_EMAIL_TEST + ".")
                .password(PASSWORD_TEST)
                .build();
        assertThrows(AuthException.class, () -> authServiceImpl.authentication(jwtRequest));
    }

}
