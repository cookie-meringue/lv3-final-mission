package finalmission.auth.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import finalmission.auth.infrastructure.AuthorizationPayload;
import finalmission.auth.infrastructure.AuthorizationPrincipal;
import finalmission.auth.infrastructure.provider.AuthorizationProvider;
import finalmission.member.domain.Member;
import finalmission.member.domain.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {AuthService.class, AuthorizationProvider.class})
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockitoBean
    private AuthorizationProvider authorizationProvider;

    @Test
    void 사용자_인증정보를_생성한다() {

        // given
        final Member member = MemberFixture.create();
        final AuthorizationPayload authorizationPayload = AuthorizationPayload.fromMember(member);
        final AuthorizationPrincipal expected = new AuthorizationPrincipal(member.getEmail());

        when(authorizationProvider.createPrincipal(authorizationPayload))
                .thenReturn(expected);

        // when
        final AuthorizationPrincipal actual = authService.createMemberPrincipal(member);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
