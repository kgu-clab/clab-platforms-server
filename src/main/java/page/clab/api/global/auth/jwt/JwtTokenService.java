package page.clab.api.global.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.memberManagement.member.domain.Role;

public interface JwtTokenService {

    TokenInfo generateToken(String id, Role role);

    Authentication getAuthentication(String token);

    String resolveToken(HttpServletRequest request);

    boolean validateToken(String token);

    boolean validateTokenSilently(String token);

    boolean isRefreshToken(String token);
}
