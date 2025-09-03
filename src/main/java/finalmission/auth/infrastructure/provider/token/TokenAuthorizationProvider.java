package finalmission.auth.infrastructure.provider.token;

import finalmission.auth.infrastructure.AuthorizationPayload;
import finalmission.auth.infrastructure.AuthorizationPrincipal;
import finalmission.auth.infrastructure.provider.AuthorizationProvider;

public abstract class TokenAuthorizationProvider implements AuthorizationProvider {

    @Override
    public final AuthorizationPrincipal createPrincipal(AuthorizationPayload payload) {
        String token = createToken(payload);
        return new AuthorizationPrincipal(token);
    }

    @Override
    public final AuthorizationPayload getPayload(AuthorizationPrincipal principal) {
        String token = principal.value();
        validateToken(token);
        return getPayload(token);
    }

    protected abstract String createToken(AuthorizationPayload payload);

    protected abstract AuthorizationPayload getPayload(String token);

    protected abstract void validateToken(String token);
}
