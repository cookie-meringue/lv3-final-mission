package finalmission.auth.infrastructure.provider.token.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.auth.infrastructure.AuthorizationPayload;
import finalmission.member.domain.Member;
import finalmission.member.domain.MemberFixture;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtAuthorizationProviderTest {

    private static final String EMAIL_KEY = "email";
    private static final Member MEMBER = MemberFixture.create();

    @Autowired
    private JwtAuthorizationProvider jwtAuthorizationProvider;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.validity-in-milliseconds}")
    private int validityInMilliseconds;

    @Test
    void jwt_를__생성한다() {
        // given
        AuthorizationPayload payload = AuthorizationPayload.fromMember(MEMBER);

        // when
        String token = jwtAuthorizationProvider.createToken(payload);
        AuthorizationPayload expected = getPayloadFromJwt(token);

        // then
        assertThat(jwtAuthorizationProvider.getPayload(token)).isEqualTo(expected);
    }

    @Test
    void jwt_를__파싱한다() {
        // given
        String token = createJwt(AuthorizationPayload.fromMember(MEMBER));

        // when
        AuthorizationPayload payload = jwtAuthorizationProvider.getPayload(token);

        // then
        assertThat(payload.email()).isEqualTo(MEMBER.getEmail());
    }

    private String createJwt(AuthorizationPayload payload) {
        Claims claims = Jwts.claims();
        claims.put(EMAIL_KEY, payload.email());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private AuthorizationPayload getPayloadFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        String email = claims.get(EMAIL_KEY, String.class);
        return new AuthorizationPayload(email);
    }
}
