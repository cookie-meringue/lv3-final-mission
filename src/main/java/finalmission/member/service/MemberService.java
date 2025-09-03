package finalmission.member.service;

import finalmission.auth.infrastructure.methodargument.MemberPrincipal;
import finalmission.exception.domain.ConflictException;
import finalmission.member.domain.Member;
import finalmission.member.infrastructure.namegenerator.NameGenerator;
import finalmission.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final NameGenerator nameGenerator;

    public Optional<Member> findByPrincipal(final MemberPrincipal principal) {
        return memberRepository.findByEmail(principal.email());
    }

    public Optional<Member> findByEmailAndPassword(
            final String email,
            final String password
    ) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public void createWithRandomName(final String email, final String password) {
        final String name = nameGenerator.generate();
        final Member member = new Member(name, email, password);
        save(member);
    }

    private Member save(final Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }
        return memberRepository.save(member);
    }
}
