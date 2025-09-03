package finalmission.auth.infrastructure.provider.token.jwt;

import finalmission.auth.infrastructure.AuthorizationPayload;
import finalmission.auth.infrastructure.provider.token.TokenAuthorizationProvider;
import finalmission.exception.domain.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthorizationProvider extends TokenAuthorizationProvider {

    private static final String EMAIL_KEY = "email";

    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtAuthorizationProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.validity-in-milliseconds}") long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    @Override
    public String createToken(AuthorizationPayload payload) {
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

    @Override
    public AuthorizationPayload getPayload(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        String name = claims.get(EMAIL_KEY, String.class);

        return new AuthorizationPayload(name);
    }

    @Override
    public void validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            claims.getBody().getExpiration();
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UnauthorizedException("인증 정보가 올바르지 않습니다.");
        }
    }
}
