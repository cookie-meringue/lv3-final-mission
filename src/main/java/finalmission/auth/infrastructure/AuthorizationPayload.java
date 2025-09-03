package finalmission.auth.infrastructure;

import finalmission.member.domain.Member;

public record AuthorizationPayload(
        String email
) {

    public static AuthorizationPayload fromMember(Member member) {
        return new AuthorizationPayload(
                member.getEmail()
        );
    }
}
