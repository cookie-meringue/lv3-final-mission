package finalmission.member.repository;

import finalmission.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(final String email, final String password);

    Optional<Member> findByEmail(final String email);

    boolean existsByEmail(final String email);
}
