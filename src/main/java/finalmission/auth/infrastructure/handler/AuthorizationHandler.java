package finalmission.auth.infrastructure.handler;

import finalmission.auth.infrastructure.AuthorizationPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface AuthorizationHandler {

    Optional<AuthorizationPrincipal> getPrincipal(final HttpServletRequest request);

    void setPrincipal(final HttpServletResponse response, final AuthorizationPrincipal principal);

    void removePrincipal(final HttpServletResponse response);
}
