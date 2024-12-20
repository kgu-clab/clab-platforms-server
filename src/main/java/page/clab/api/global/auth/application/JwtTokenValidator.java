package page.clab.api.global.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenValidator {

    private final Key key;
    private final long refreshTokenDuration;
    private final JwtTokenParser tokenParser;

    public JwtTokenValidator(
        @Value("${security.jwt.secret-key}") String secretKey,
        @Value("${security.jwt.token-validity-in-seconds.refresh-token}") long refreshTokenDuration,
        JwtTokenParser tokenParser
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.refreshTokenDuration = refreshTokenDuration;
        this.tokenParser = tokenParser;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.");
        }
        return false;
    }

    public boolean validateTokenSilently(String token) {
        try {
            Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = tokenParser.parseClaims(token);
            Date issuedAt = claims.getIssuedAt();
            Date expiration = claims.getExpiration();
            if (issuedAt != null && expiration != null) {
                long duration = expiration.getTime() - issuedAt.getTime();
                return duration == refreshTokenDuration;
            }
        } catch (Exception e) {
            log.debug("Failed to check if the token is a refresh token", e);
        }
        return false;
    }
}
