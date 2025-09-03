package finalmission.auth.infrastructure.handler.token.cookie;

import finalmission.auth.infrastructure.handler.token.TokenAuthorizationHandler;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieTokenAuthorizationHandler extends TokenAuthorizationHandler {

    private static final String TOKEN_NAME = "token";
    private final int maxAge;

    public CookieTokenAuthorizationHandler(
            @Value("${jwt.validity-in-milliseconds}") int maxAge
    ) {
        this.maxAge = maxAge / 1000;
    }

    @Override
    public Optional<String> getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .map(Cookie::getValue)
                .findFirst();
    }

    @Override
    public void setToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void removeToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
