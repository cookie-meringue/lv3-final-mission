package finalmission.auth.controller;

import finalmission.auth.dto.request.LoginRequest;
import finalmission.auth.dto.request.SignUpRequest;
import finalmission.auth.infrastructure.AuthorizationPrincipal;
import finalmission.auth.infrastructure.handler.AuthorizationHandler;
import finalmission.auth.service.facade.AuthServiceFacade;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthServiceFacade authServiceFacade;
    private final AuthorizationHandler authorizationHandler;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final SignUpRequest request) {
        authServiceFacade.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            final HttpServletResponse response,
            @RequestBody @Valid final LoginRequest request
    ) {
        AuthorizationPrincipal principal = authServiceFacade.createAuthorizationPrincipal(request);
        authorizationHandler.setPrincipal(response, principal);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        authorizationHandler.removePrincipal(response);
        return ResponseEntity.ok().build();
    }
}
