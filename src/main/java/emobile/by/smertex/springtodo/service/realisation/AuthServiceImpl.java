package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.database.entity.nosql.UserJwt;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import emobile.by.smertex.springtodo.dto.security.JwtRequest;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.service.exception.AuthException;
import emobile.by.smertex.springtodo.service.interfaces.AuthService;
import emobile.by.smertex.springtodo.service.interfaces.LoadUserService;
import emobile.by.smertex.springtodo.service.interfaces.UserJwtService;
import emobile.by.smertex.springtodo.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final LoadUserService loadUserService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtTokenUtils;

    private final UserJwtService userJwtService;

    @Override
    @CacheEvict(value = "users", key = "#authRequest.username()")
    public String authentication(JwtRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new AuthException(e.getMessage());
        }
        return generateAccess(authRequest);
    }

    @Override
    public Optional<SecurityUserDto> takeUserFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.of(new SecurityUserDto(
                (String) authentication.getPrincipal(),
                authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(Role.ADMIN.getEditedRole()))));
    }

    private String generateAccess(JwtRequest authRequest){
        UserDetails userDetails = loadUserService.loadUserByUsername(authRequest.username());
        String token = jwtTokenUtils.generateToken(userDetails);
        userJwtService.save(UserJwt.builder()
                .jwt(token)
                .email(userDetails.getUsername())
                .roles(jwtTokenUtils.getRoles(token)
                        .stream()
                        .map(Role::deletePrefix)
                        .map(Role::valueOf)
                        .toList())
                .build());
        return token;
    }
}
