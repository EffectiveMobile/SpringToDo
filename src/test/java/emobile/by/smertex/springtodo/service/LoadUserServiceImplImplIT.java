package emobile.by.smertex.springtodo.service;

import emobile.by.smertex.springtodo.annotation.IT;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import emobile.by.smertex.springtodo.service.realisation.LoadUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
public class LoadUserServiceImplImplIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final String PASSWORD_TEST = "qwertyui12345678";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final LoadUserServiceImpl loadUserServiceImpl;

    @Test
    void loadUserByUsernameForAdmin(){
        UserDetails userDetails = loadUserServiceImpl.loadUserByUsername(ADMIN_EMAIL_TEST);
        assertEquals(ADMIN_EMAIL_TEST, userDetails.getUsername());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD_TEST, userDetails.getPassword()));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(Role.ADMIN.getEditedRole())));
    }

    @Test
    void loadUserByUsernameForUser(){
        UserDetails userDetails = loadUserServiceImpl.loadUserByUsername(USER_EMAIL_TEST);
        assertEquals(USER_EMAIL_TEST, userDetails.getUsername());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD_TEST, userDetails.getPassword()));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(Role.USER.getEditedRole())));
    }

    @Test
    void loadUserException(){
        assertThrows(UsernameNotFoundException.class,
                () -> loadUserServiceImpl.loadUserByUsername(USER_EMAIL_TEST + "."));
    }
}
