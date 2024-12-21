package page.clab.api.global.auth.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.config.JwtTokenProperties;

@Component
public class JwtTokenGenerator {

    private final Key key;
    private final long accessTokenDuration;
    private final RefreshTokenDurationProvider refreshTokenDurationProvider;

    public JwtTokenGenerator(
        JwtTokenProperties jwtTokenProperties,
        RefreshTokenDurationProvider refreshTokenDurationProvider
    ) {
        this.key = Keys.hmacShaKeyFor(jwtTokenProperties.getSecretKey().getBytes());
        this.accessTokenDuration = jwtTokenProperties.getAccessTokenDuration();
        this.refreshTokenDurationProvider = refreshTokenDurationProvider;
    }

    /**
     * 액세스 토큰과 권한별 리프레시 토큰을 생성하여 TokenInfo 객체로 반환합니다.
     *
     * @param id   사용자 ID
     * @param role 사용자 역할
     * @return TokenInfo 객체
     */
    public TokenInfo generateToken(String id, Role role) {
        Date now = new Date();

        String accessToken = createJwtToken(id, role, now, accessTokenDuration);

        long refreshTokenDuration = refreshTokenDurationProvider.getRefreshTokenDuration(role);
        String refreshToken = createJwtToken(id, role, now, refreshTokenDuration);

        return TokenInfo.create(accessToken, refreshToken);
    }

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param id       사용자 ID
     * @param role     사용자 역할
     * @param issuedAt 발급 시간
     * @param duration 토큰 유효 기간 (밀리초)
     * @return JWT 토큰 문자열
     */
    private String createJwtToken(String id, Role role, Date issuedAt, long duration) {
        Date expiration = new Date(issuedAt.getTime() + duration);
        return Jwts.builder()
            .subject(id)
            .claim("role", role == null ? Role.GUEST.name() : role.name())
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(key)
            .compact();
    }
}
