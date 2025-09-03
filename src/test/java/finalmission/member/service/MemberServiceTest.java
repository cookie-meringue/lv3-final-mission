package finalmission.member.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.auth.infrastructure.methodargument.MemberPrincipal;
import finalmission.member.domain.Member;
import finalmission.member.domain.MemberFixture;
import finalmission.member.infrastructure.namegenerator.NameGenerator;
import finalmission.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {MemberService.class, MemberRepository.class, NameGenerator.class})
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockitoBean
    private MemberRepository memberRepository;

    @MockitoBean
    private NameGenerator nameGenerator;

    @Test
    void 사용자_인증정보를_통해_사용자를_반환한다() {
        // given
        final Member member = MemberFixture.create();
        final MemberPrincipal memberPrincipal = new MemberPrincipal(member.getEmail());
        when(memberRepository.findByEmail(member.getEmail()))
                .thenReturn(Optional.of(member));
        final Optional<Member> expected = Optional.of(member);

        // when
        final Optional<Member> actual = memberService.findByPrincipal(memberPrincipal);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 이메일과_비밀번호를_통해_사용자를_반환한다() {
        // given
        final Member member = MemberFixture.create();
        when(memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword()))
                .thenReturn(Optional.of(member));
        final Optional<Member> expected = Optional.of(member);

        // when
        final Optional<Member> actual = memberService.findByEmailAndPassword(member.getEmail(), member.getPassword());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 랜덤한_이름으로_사용자를_생성한다() {
        // given
        final String name = "Hello";
        final Member member = new Member(name, "email@email.com", "password");
        when(nameGenerator.generate())
                .thenReturn(name);
        when(memberRepository.existsByEmail(member.getEmail()))
                .thenReturn(false);

        // when
        memberService.createWithRandomName(member.getEmail(), member.getPassword());

        // then
        verify(memberRepository).save(member);
    }
}
