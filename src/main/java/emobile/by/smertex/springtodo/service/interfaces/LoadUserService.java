package emobile.by.smertex.springtodo.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Сервис, обеспечивающий создание экземпляра UserDetails, который необходим для работы Spring Security
 */
public interface LoadUserService extends UserDetailsService {
}
