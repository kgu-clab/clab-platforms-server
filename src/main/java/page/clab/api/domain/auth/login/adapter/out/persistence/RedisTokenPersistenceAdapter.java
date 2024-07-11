package page.clab.api.domain.auth.login.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.login.application.port.out.RegisterRedisTokenPort;
import page.clab.api.domain.auth.login.application.port.out.RemoveRedisTokenPort;
import page.clab.api.domain.auth.login.application.port.out.RetrieveRedisTokenPort;
import page.clab.api.domain.auth.login.domain.RedisToken;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisTokenPersistenceAdapter implements
        RegisterRedisTokenPort,
        RetrieveRedisTokenPort,
        RemoveRedisTokenPort {

    private final RedisTokenRepository redisTokenRepository;

    @Override
    public RedisToken save(RedisToken redisToken) {
        return redisTokenRepository.save(redisToken);
    }

    @Override
    public Optional<RedisToken> findById(String id) {
        return redisTokenRepository.findById(id);
    }

    @Override
    public Optional<RedisToken> findByAccessToken(String accessToken) {
        return redisTokenRepository.findByAccessToken(accessToken);
    }

    @Override
    public Optional<RedisToken> findByRefreshToken(String refreshToken) {
        return redisTokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public void delete(RedisToken redisToken) {
        redisTokenRepository.delete(redisToken);
    }

    @Override
    public void deleteByMemberId(String memberId) {
        redisTokenRepository.deleteById(memberId);
    }

    @Override
    public List<RedisToken> findAll() {
        return (List<RedisToken>) redisTokenRepository.findAll();
    }
}
