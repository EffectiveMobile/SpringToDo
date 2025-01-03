package emobile.by.smertex.springtodo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import emobile.by.smertex.springtodo.dto.exception.ApplicationResponse;
import emobile.by.smertex.springtodo.util.JwtTokenUtils;
import emobile.by.smertex.springtodo.util.ResponseMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Фильтр-сервлет, отвечающий за авторизацию пользователя в системе. Сохраняет пользователя в контексте приложения в случае валидации JWT
 * до тех пор, пока запрос не будет завершен. После пользователь снова будет проходить проверку и сохранение в контексте. В случае, если
 * пользователь не прошел проверку, то ему возвращается соответствующий ответ
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7);
            try{
                username = jwtTokenUtils.getUsername(jwt);
            } catch (ExpiredJwtException e){
                response.getWriter().write(objectMapper.writeValueAsString(responseException(response, ResponseMessage.EXPIRED_JWT_EXCEPTION)));
            } catch (SignatureException e){
                response.getWriter().write(objectMapper.writeValueAsString(responseException(response, ResponseMessage.SIGNATURE_EXCEPTION)));
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenUtils.getRoles(jwt).stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList());
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    private ApplicationResponse responseException(HttpServletResponse response, String message) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return new ApplicationResponse(message, HttpStatus.UNAUTHORIZED, LocalDateTime.now());
    }
}