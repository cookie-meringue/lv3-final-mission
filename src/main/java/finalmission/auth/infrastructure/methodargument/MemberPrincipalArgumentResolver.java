package finalmission.auth.infrastructure.methodargument;

import finalmission.auth.infrastructure.AuthorizationPayload;
import finalmission.auth.infrastructure.methodargument.context.AuthorizationPayloadContext;
import finalmission.exception.domain.UnauthorizedException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationPayloadContext authorizationPayloadContext;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthorizedMember.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        AuthorizationPayload authorizationPayload = authorizationPayloadContext.get()
                .orElseThrow(() -> new UnauthorizedException("로그인 정보가 없습니다."));

        return new MemberPrincipal(authorizationPayload.email());
    }
}
