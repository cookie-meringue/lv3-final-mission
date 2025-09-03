package finalmission.auth.infrastructure.methodargument.context;

import finalmission.auth.infrastructure.AuthorizationPayload;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Setter
@Component
@RequestScope
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationPayloadContext {

    private AuthorizationPayload payload;

    public Optional<AuthorizationPayload> get() {
        return Optional.ofNullable(payload);
    }
}
