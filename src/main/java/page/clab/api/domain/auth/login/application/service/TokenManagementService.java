package page.clab.api.domain.auth.login.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.login.application.dto.request.LoginRequestDto;
import page.clab.api.domain.auth.login.application.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.auth.login.application.dto.response.LoginResult;
import page.clab.api.domain.auth.login.application.dto.response.TokenHeader;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.application.exception.LoginFailedException;
import page.clab.api.domain.auth.login.application.exception.MemberLockedException;
import page.clab.api.domain.auth.login.application.port.in.ManageLoginUseCase;
import page.clab.api.domain.auth.login.application.port.in.ManageRedisTokenUseCase;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.auth.exception.TokenForgeryException;
import page.clab.api.global.auth.exception.TokenMisuseException;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.util.HttpReqResUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("tokenManagementService")
public class TokenManagementService implements ManageLoginUseCase {

    private final ManageRedisTokenUseCase manageRedisTokenUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public TokenHeader reissueToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        RedisToken redisToken = manageRedisTokenUseCase.findByRefreshToken(refreshToken);

        validateMemberExistence(authentication);
        validateToken(redisToken);

        TokenInfo newTokenInfo = jwtTokenProvider.generateToken(redisToken.getMemberId(), redisToken.getRole());
        manageRedisTokenUseCase.saveToken(redisToken.getMemberId(), redisToken.getRole(), newTokenInfo, redisToken.getIp());
        return TokenHeader.create(newTokenInfo);
    }

    @Override
    public List<String> retrieveCurrentLoggedInUsers() {
        return manageRedisTokenUseCase.getCurrentLoggedInUsers();
    }

    private void validateMemberExistence(Authentication authentication) {
        String id = authentication.getName();
        if (!externalRetrieveMemberUseCase.existsById(id)) {
            throw new TokenForgeryException("존재하지 않는 회원에 대한 토큰입니다.");
        }
    }

    private void validateToken(RedisToken redisToken) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (!redisToken.isSameIp(clientIpAddress)) {
            manageRedisTokenUseCase.deleteByAccessToken(redisToken.getAccessToken());
            throw new TokenMisuseException("[" + clientIpAddress + "] 토큰 발급 IP와 다른 IP에서 발급을 시도하여 토큰을 삭제하였습니다.");
        }
    }

    @Override
    public LoginResult login(HttpServletRequest request, LoginRequestDto requestDto) throws LoginFailedException, MemberLockedException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public LoginResult authenticate(HttpServletRequest request, TwoFactorAuthenticationRequestDto requestDto) throws LoginFailedException, MemberLockedException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String resetAuthenticator(String memberId) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
