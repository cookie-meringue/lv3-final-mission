package finalmission.auth.infrastructure.methodargument.config;

import finalmission.auth.infrastructure.methodargument.AuthorizationPrincipalInterceptor;
import finalmission.auth.infrastructure.methodargument.MemberPrincipalArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthorizationMethodArgumentConfig implements WebMvcConfigurer {

    private final AuthorizationPrincipalInterceptor authorizationPrincipalInterceptor;
    private final MemberPrincipalArgumentResolver memberPrincipalArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberPrincipalArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationPrincipalInterceptor)
                .addPathPatterns("/**");
    }
}
