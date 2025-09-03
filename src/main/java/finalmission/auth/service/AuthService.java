package finalmission.auth.service;

import finalmission.auth.infrastructure.AuthorizationPayload;
import finalmission.auth.infrastructure.AuthorizationPrincipal;
import finalmission.auth.infrastructure.provider.AuthorizationProvider;
import finalmission.member.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthorizationProvider authorizationProvider;

    public AuthorizationPrincipal createMemberPrincipal(final Member member) {
        return authorizationProvider.createPrincipal(AuthorizationPayload.fromMember(member));
    }
}
