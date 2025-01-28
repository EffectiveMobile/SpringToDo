package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.User;
import emobile.by.smertex.springtodo.database.repository.sql.UserRepository;
import emobile.by.smertex.springtodo.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
