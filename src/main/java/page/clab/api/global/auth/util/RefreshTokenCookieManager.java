package page.clab.api.global.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;
import page.clab.api.global.config.RefreshTokenCookieProperties;

/**
 * 리프레시 토큰 쿠키를 생성, 설정, 삭제 및 가져오는 매니저 클래스.
 */
@Component
public class RefreshTokenCookieManager {

    private final String cookieName;
    private final String cookiePath;
    private final boolean httpOnly;
    private final boolean secure;
    private final int maxAge;
    private final String sameSite;

    public RefreshTokenCookieManager(
        RefreshTokenCookieProperties refreshTokenCookieProperties
    ) {
        this.cookieName = refreshTokenCookieProperties.getName();
        this.cookiePath = refreshTokenCookieProperties.getPath();
        this.httpOnly = refreshTokenCookieProperties.isHttpOnly();
        this.secure = refreshTokenCookieProperties.isSecure();
        this.maxAge = refreshTokenCookieProperties.getMaxAge();
        this.sameSite = refreshTokenCookieProperties.getSameSite();
    }

    /**
     * 리프레시 토큰 쿠키를 생성하여 응답에 추가합니다.
     *
     * @param response     HttpServletResponse 객체
     * @param refreshToken 생성된 리프레시 토큰 문자열
     */
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(cookieName, refreshToken);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", sameSite);
        response.addCookie(cookie);
    }

    /**
     * 리프레시 토큰 쿠키를 삭제합니다.
     *
     * @param response HttpServletResponse 객체
     */
    public void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0); // 쿠키 만료
        cookie.setAttribute("SameSite", sameSite);
        response.addCookie(cookie);
    }

    /**
     * 요청에서 리프레시 토큰을 가져옵니다.
     *
     * @param request HttpServletRequest 객체
     * @return Optional으로 감싼 리프레시 토큰 문자열. 없으면 Optional.empty() 반환
     */
    public Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookieName.equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue);
    }
}
