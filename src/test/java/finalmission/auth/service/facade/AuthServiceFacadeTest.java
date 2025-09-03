package finalmission.auth.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.auth.dto.request.LoginRequest;
import finalmission.auth.dto.request.SignUpRequest;
import finalmission.auth.infrastructure.AuthorizationPrincipal;
import finalmission.auth.service.AuthService;
import finalmission.member.domain.Member;
import finalmission.member.domain.MemberFixture;
import finalmission.member.service.MemberService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class AuthServiceFacadeTest {

    @Autowired
    private AuthServiceFacade authServiceFacade;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private MemberService memberService;

    @Test
    void 회원가입을_할_수_있다() {

        // given
        final String email = "email@email.com";
        final String password = "password";
        final SignUpRequest request = new SignUpRequest(email, password);

        // when
        authServiceFacade.signUp(request);

        // then
        verify(memberService).createWithRandomName(email, password);
    }

    @Test
    void 사용자_인증정보를_생성한다() {

        // given
        final Member member = MemberFixture.create();
        final LoginRequest request = new LoginRequest(member.getEmail(), member.getPassword());
        final AuthorizationPrincipal expected = new AuthorizationPrincipal(member.getEmail());

        when(memberService.findByEmailAndPassword(member.getEmail(), member.getPassword()))
                .thenReturn(Optional.of(member));
        when(authService.createMemberPrincipal(member))
                .thenReturn(expected);

        // when
        final AuthorizationPrincipal actual = authServiceFacade.createAuthorizationPrincipal(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
