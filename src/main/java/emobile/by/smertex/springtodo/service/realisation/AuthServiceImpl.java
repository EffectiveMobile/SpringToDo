package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.database.entity.realisation.enums.Role;
import emobile.by.smertex.springtodo.dto.exception.ApplicationResponse;
import emobile.by.smertex.springtodo.dto.security.JwtRequest;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.service.interfaces.AuthService;
import emobile.by.smertex.springtodo.service.interfaces.LoadUserService;
import emobile.by.smertex.springtodo.util.JwtTokenUtils;
import emobile.by.smertex.springtodo.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final LoadUserService loadUserService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    @CacheEvict(value = "users", key = "#authRequest.username()")
    public ResponseEntity<?> authentication(JwtRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(new ApplicationResponse(ResponseMessage.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED, LocalDateTime.now()));
        }
        UserDetails userDetails = loadUserService.loadUserByUsername(authRequest.username());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    @Override
    public Optional<SecurityUserDto> takeUserFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.of(new SecurityUserDto(
                (String) authentication.getPrincipal(),
                authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(Role.ADMIN.getEditedRole()))));
    }
}
