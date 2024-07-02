package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.RedisToken;

import java.util.Optional;

public interface LoadRedisTokenPort {
    Optional<RedisToken> findById(String id);
    Optional<RedisToken> findByAccessToken(String accessToken);
    Optional<RedisToken> findByRefreshToken(String refreshToken);
}
