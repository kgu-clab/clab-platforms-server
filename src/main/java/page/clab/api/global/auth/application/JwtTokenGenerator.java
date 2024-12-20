package page.clab.api.global.auth.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.memberManagement.member.domain.Role;

@Component
public class JwtTokenGenerator {

    private final Key key;
    private final long accessTokenDuration;
    private final long refreshTokenDuration;

    public JwtTokenGenerator(
        @Value("${security.jwt.secret-key}") String secretKey,
        @Value("${security.jwt.token-validity-in-seconds.access-token}") long accessTokenDuration,
        @Value("${security.jwt.token-validity-in-seconds.refresh-token}") long refreshTokenDuration
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenDuration = accessTokenDuration;
        this.refreshTokenDuration = refreshTokenDuration;
    }

    public TokenInfo generateToken(String id, Role role) {
        Date expiry = new Date();
        Date accessTokenExpiry = new Date(expiry.getTime() + (accessTokenDuration));
        String accessToken = Jwts.builder()
            .subject(id)
            .claim("role", role == null ? Role.GUEST.name() : role.name())
            .issuedAt(expiry)
            .expiration(accessTokenExpiry)
            .signWith(key)
            .compact();

        Date refreshTokenExpiry = new Date(expiry.getTime() + (refreshTokenDuration));
        String refreshToken = Jwts.builder()
            .subject(id)
            .claim("role", role == null ? Role.GUEST.name() : role.name())
            .issuedAt(expiry)
            .expiration(refreshTokenExpiry)
            .signWith(key)
            .compact();

        return TokenInfo.create(accessToken, refreshToken);
    }
}
