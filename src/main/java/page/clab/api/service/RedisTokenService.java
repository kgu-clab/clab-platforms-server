package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.RedisTokenRepository;
import page.clab.api.type.dto.TokenInfo;
import page.clab.api.type.entity.RedisToken;
import page.clab.api.type.etc.Role;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTokenRepository redisTokenRepository;

    public RedisToken getRedisTokenByAccessToken(String accessToken) {
        return redisTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 토큰입니다."));
    }

    public RedisToken getRedisTokenByRefreshToken(String refreshToken) {
        return redisTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 토큰입니다."));
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
                .ifPresent(redisToken -> redisTokenRepository.delete(redisToken));
    }

}