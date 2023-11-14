package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.RedisTokenRepository;
import page.clab.api.type.entity.RedisToken;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTokenRepository redisTokenRepository;

    @Transactional
    public RedisToken getRedisToken(String accessToken) {
        return redisTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 토큰입니다."));
    }

    @Transactional
    public void saveRedisToken(String memberId, String accessToken, String refreshToken) {
        RedisToken tokenInfo = new RedisToken(memberId, accessToken, refreshToken);
        redisTokenRepository.save(tokenInfo);
    }

    @Transactional
    public void deleteRedisTokenByAccessToken(String accessToken) {
        redisTokenRepository.findByAccessToken(accessToken)
                .ifPresent(redisToken -> redisTokenRepository.delete(redisToken));
    }

}