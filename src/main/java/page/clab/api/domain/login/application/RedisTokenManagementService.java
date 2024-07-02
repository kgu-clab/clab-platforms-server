package page.clab.api.domain.login.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.application.port.in.RedisTokenManagementUseCase;
import page.clab.api.domain.login.application.port.out.LoadRedisTokenPort;
import page.clab.api.domain.login.application.port.out.RegisterRedisTokenPort;
import page.clab.api.domain.login.application.port.out.RemoveRedisTokenPort;
import page.clab.api.domain.login.application.port.out.RetrieveAllRedisTokensPort;
import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.global.auth.exception.TokenNotFoundException;
import page.clab.api.global.auth.jwt.JwtTokenProvider;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RedisTokenManagementService implements RedisTokenManagementUseCase {

    private final RegisterRedisTokenPort registerRedisTokenPort;
    private final LoadRedisTokenPort loadRedisTokenPort;
    private final RemoveRedisTokenPort removeRedisTokenPort;
    private final RetrieveAllRedisTokensPort retrieveAllRedisTokensPort;

    private final JwtTokenProvider jwtTokenProvider;

    public RedisToken findByAccessToken(String accessToken) {
        return loadRedisTokenPort.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public RedisToken findByRefreshToken(String refreshToken) {
        return loadRedisTokenPort.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public List<String> getCurrentLoggedInUsers() {
        Iterable<RedisToken> iterableTokens = retrieveAllRedisTokensPort.findAll();
        return StreamSupport.stream(iterableTokens.spliterator(), false)
                .filter(redisToken -> jwtTokenProvider.validateTokenSilently(redisToken.getAccessToken()))
                .map(RedisToken::getId)
                .distinct()
                .collect(Collectors.toList());
    }

    public void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip) {
        RedisToken redisToken = RedisToken.create(memberId, role, ip, tokenInfo);
        registerRedisTokenPort.save(redisToken);
    }

    public void deleteByAccessToken(String accessToken) {
        loadRedisTokenPort.findByAccessToken(accessToken)
                .ifPresent(removeRedisTokenPort::delete);
    }

    public void deleteByMemberId(String memberId) {
        removeRedisTokenPort.deleteByMemberId(memberId);
    }
}
