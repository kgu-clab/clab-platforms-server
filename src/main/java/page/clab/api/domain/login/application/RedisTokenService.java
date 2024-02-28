package page.clab.api.domain.login.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.dao.RedisTokenRepository;
import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.global.auth.exception.TokenNotFoundException;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTokenRepository redisTokenRepository;

    public RedisToken getRedisTokenByAccessToken(String accessToken) {
        return redisTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public RedisToken getRedisTokenByRefreshToken(String refreshToken) {
        return redisTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰입니다."));
    }

    public void saveRedisToken(String memberId, Role role, TokenInfo tokenInfo, String ip) {
        RedisToken redisToken = RedisToken.builder()
                .id(memberId)
                .role(role)
                .ip(ip)
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
        redisTokenRepository.save(redisToken);
    }

    public void deleteRedisTokenByAccessToken(String accessToken) {
        redisTokenRepository.findByAccessToken(accessToken)
                .ifPresent(redisTokenRepository::delete);
    }

    public void deleteRedisTokenByMemberId(String memberId) {
        redisTokenRepository.findById(memberId)
                .ifPresent(redisTokenRepository::delete);
    }

}