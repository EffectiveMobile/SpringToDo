package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.service.exception.UserNotFoundInDatabaseException;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.Metainfo;
import emobile.by.smertex.springtodo.database.repository.sql.MetainfoRepository;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.service.interfaces.AuthService;
import emobile.by.smertex.springtodo.service.interfaces.MetainfoService;
import emobile.by.smertex.springtodo.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetainfoServiceImpl implements MetainfoService {

    private final UserService userService;

    private final AuthService authService;

    private final MetainfoRepository metainfoRepository;

    @Override
    @Transactional
    public Optional<Metainfo> save() {
        SecurityUserDto securityUserDto = authService.takeUserFromContext()
                .orElseThrow();
        return Optional.of(Metainfo.builder()
                .createdBy(userService.findByEmail(securityUserDto.email())
                        .orElseThrow(() -> new UserNotFoundInDatabaseException(securityUserDto.email())))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()).map(metainfoRepository::save);
    }

    @Override
    public Metainfo update(Metainfo metainfo) {
        return metainfoRepository.saveAndFlush(metainfo);
    }

}
