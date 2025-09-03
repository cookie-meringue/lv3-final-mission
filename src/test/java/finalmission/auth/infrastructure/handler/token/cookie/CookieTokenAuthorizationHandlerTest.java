package finalmission.auth.infrastructure.handler.token.cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = CookieTokenAuthorizationHandler.class)
class CookieTokenAuthorizationHandlerTest {

    private static final String TOKEN_NAME = "token";
    private static final String TOKEN_VALUE = "testToken";
    private static final int MAX_AGE = 3600;

    @Autowired
    private CookieTokenAuthorizationHandler cookieTokenAuthorizationHandler;

    @Test
    void 쿠키에서_인증_정보를_추출한다() {

        // given
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Cookie cookieWithToken = new Cookie(TOKEN_NAME, TOKEN_VALUE);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{cookieWithToken});

        // when
        String token = cookieTokenAuthorizationHandler.getToken(httpServletRequest).get();

        // then
        assertThat(token).isEqualTo(TOKEN_VALUE);
    }

    @Test
    void 쿠키에_인증_정보를_넣는다() {

        // given
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        String token = TOKEN_VALUE;

        Cookie expectedCookie = createCookie(TOKEN_VALUE, MAX_AGE);

        // when
        cookieTokenAuthorizationHandler.setToken(httpServletResponse, token);

        // then
        Mockito.verify(httpServletResponse)
                .addCookie(Mockito.argThat(cookie -> cookie.equals(expectedCookie)));
    }

    @Test
    void 쿠키에서_인증_정보를_삭제한다() {

        // given
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        Cookie expectedCookie = createCookie(null, 0);

        // when
        cookieTokenAuthorizationHandler.removeToken(httpServletResponse);

        // then
        Mockito.verify(httpServletResponse)
                .addCookie(Mockito.argThat(cookie -> cookie.equals(expectedCookie)));
    }

    private Cookie createCookie(String value, int maxAge) {
        Cookie cookie = new Cookie(TOKEN_NAME, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }
}
