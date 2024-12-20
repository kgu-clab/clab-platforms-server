package page.clab.api.global.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.memberManagement.member.domain.Role;

@Component
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenGenerator tokenGenerator;
    private final JwtTokenParser tokenParser;
    private final JwtTokenValidator tokenValidator;

    @Override
    public TokenInfo generateToken(String id, Role role) {
        return tokenGenerator.generateToken(id, role);
    }

    @Override
    public Authentication getAuthentication(String token) {
        return tokenParser.getAuthentication(token);
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        return tokenParser.resolveToken(request);
    }

    @Override
    public boolean validateToken(String token) {
        return tokenValidator.validateToken(token);
    }

    @Override
    public boolean validateTokenSilently(String token) {
        return tokenValidator.validateTokenSilently(token);
    }

    @Override
    public boolean isRefreshToken(String token) {
        return tokenValidator.isRefreshToken(token);
    }
}
