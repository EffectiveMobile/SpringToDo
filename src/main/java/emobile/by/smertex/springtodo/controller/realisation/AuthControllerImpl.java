package emobile.by.smertex.springtodo.controller.realisation;

import emobile.by.smertex.springtodo.controller.interfaces.AuthController;
import emobile.by.smertex.springtodo.dto.security.JwtRequest;
import emobile.by.smertex.springtodo.service.interfaces.AuthService;
import emobile.by.smertex.springtodo.util.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.AUTH_PATH)
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return authService.authentication(authRequest);
    }
}
