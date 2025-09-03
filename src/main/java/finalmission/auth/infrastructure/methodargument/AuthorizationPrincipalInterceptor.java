package finalmission.auth.infrastructure.methodargument;

import finalmission.auth.infrastructure.handler.AuthorizationHandler;
import finalmission.auth.infrastructure.methodargument.context.AuthorizationPayloadContext;
import finalmission.auth.infrastructure.provider.AuthorizationProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class AuthorizationPrincipalInterceptor implements HandlerInterceptor {

    private final AuthorizationHandler authorizationHandler;
    private final AuthorizationProvider authorizationProvider;
    private final AuthorizationPayloadContext authorizationPayloadContext;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        authorizationHandler.getPrincipal(request).ifPresentOrElse(
                principal -> authorizationPayloadContext.setPayload(authorizationProvider.getPayload(principal)),
                () -> authorizationPayloadContext.setPayload(null)
        );
        return true;
    }
}
