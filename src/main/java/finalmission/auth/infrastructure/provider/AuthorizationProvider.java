package finalmission.auth.infrastructure.provider;

import finalmission.auth.infrastructure.AuthorizationPayload;
import finalmission.auth.infrastructure.AuthorizationPrincipal;

public interface AuthorizationProvider {

    AuthorizationPrincipal createPrincipal(AuthorizationPayload payload);

    AuthorizationPayload getPayload(AuthorizationPrincipal principal);
}
