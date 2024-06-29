package page.clab.api.domain.login.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.application.RedisTokenManagementService;
import page.clab.api.domain.login.dao.RedisTokenRepository;
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
public class RedisTokenManagementServiceImpl implements RedisTokenManagementService {

    private final RedisTokenRepository redisTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public RedisToken findByAccessToken(String accessToken) {
        return redisTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public RedisToken findByRefreshToken(String refreshToken) {
        return redisTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public List<String> getCurrentLoggedInUsers() {
        Iterable<RedisToken> iterableTokens = redisTokenRepository.findAll();
        return StreamSupport.stream(iterableTokens.spliterator(), false)
                .filter(redisToken -> jwtTokenProvider.validateTokenSilently(redisToken.getAccessToken()))
                .map(RedisToken::getId)
                .distinct()
                .collect(Collectors.toList());
    }

    public void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip) {
        RedisToken redisToken = RedisToken.create(memberId, role, ip, tokenInfo);
        redisTokenRepository.save(redisToken);
    }

    public void deleteByAccessToken(String accessToken) {
        redisTokenRepository.findByAccessToken(accessToken)
                .ifPresent(redisTokenRepository::delete);
    }

    public void deleteByMemberId(String memberId) {
        redisTokenRepository.findById(memberId)
                .ifPresent(redisTokenRepository::delete);
    }
}