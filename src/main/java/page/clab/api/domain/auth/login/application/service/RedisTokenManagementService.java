package page.clab.api.domain.auth.login.application.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.application.port.in.ManageRedisTokenUseCase;
import page.clab.api.domain.auth.login.application.port.out.RegisterRedisTokenPort;
import page.clab.api.domain.auth.login.application.port.out.RemoveRedisTokenPort;
import page.clab.api.domain.auth.login.application.port.out.RetrieveRedisTokenPort;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.auth.application.JwtTokenService;
import page.clab.api.global.auth.exception.TokenNotFoundException;

@Service
@RequiredArgsConstructor
public class RedisTokenManagementService implements ManageRedisTokenUseCase {

    private final RegisterRedisTokenPort registerRedisTokenPort;
    private final RetrieveRedisTokenPort retrieveRedisTokenPort;
    private final RemoveRedisTokenPort removeRedisTokenPort;
    private final JwtTokenService jwtTokenService;

    public RedisToken findByAccessToken(String accessToken) {
        return retrieveRedisTokenPort.findByAccessToken(accessToken)
            .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public RedisToken findByRefreshToken(String refreshToken) {
        return retrieveRedisTokenPort.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public List<String> getCurrentLoggedInUsers() {
        Iterable<RedisToken> iterableTokens = retrieveRedisTokenPort.findAll();
        return StreamSupport.stream(iterableTokens.spliterator(), false)
            .filter(Objects::nonNull)
            .filter(redisToken -> jwtTokenService.validateTokenSilently(redisToken.getAccessToken()))
            .map(RedisToken::getMemberId)
            .distinct()
            .toList();
    }

    public void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip) {
        RedisToken redisToken = RedisToken.create(memberId, role, ip, tokenInfo);
        registerRedisTokenPort.save(redisToken);
    }

    public void deleteByAccessToken(String accessToken) {
        retrieveRedisTokenPort.findByAccessToken(accessToken)
            .ifPresent(removeRedisTokenPort::delete);
    }
}
