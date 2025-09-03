package finalmission.auth.service.facade;

import finalmission.auth.dto.request.LoginRequest;
import finalmission.auth.dto.request.SignUpRequest;
import finalmission.auth.infrastructure.AuthorizationPrincipal;
import finalmission.auth.service.AuthService;
import finalmission.exception.domain.UnauthorizedException;
import finalmission.member.domain.Member;
import finalmission.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceFacade {

    private final AuthService authService;
    private final MemberService memberService;

    @Transactional
    public void signUp(final SignUpRequest request) {
        memberService.createWithRandomName(
                request.email(),
                request.password()
        );
    }

    @Transactional(readOnly = true)
    public AuthorizationPrincipal createAuthorizationPrincipal(final LoginRequest request) {
        final Member member = memberService.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new UnauthorizedException("이메일 혹은 비밀번호가 일치하지 않습니다."));

        return authService.createMemberPrincipal(member);
    }
}
