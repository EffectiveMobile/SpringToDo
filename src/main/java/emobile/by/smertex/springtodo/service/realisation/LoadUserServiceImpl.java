package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.service.interfaces.LoadUserService;
import emobile.by.smertex.springtodo.service.interfaces.UserService;
import emobile.by.smertex.springtodo.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoadUserServiceImpl implements LoadUserService {

    private final UserService userService;

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.findByEmail(email)
                .map(user -> User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND_EXCEPTION));
    }
}
