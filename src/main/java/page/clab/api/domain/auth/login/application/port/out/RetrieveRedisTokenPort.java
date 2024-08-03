package page.clab.api.domain.auth.login.application.port.out;

import page.clab.api.domain.auth.login.domain.RedisToken;

import java.util.List;
import java.util.Optional;

public interface RetrieveRedisTokenPort {

    Optional<RedisToken> findByAccessToken(String accessToken);

    Optional<RedisToken> findByRefreshToken(String refreshToken);

    List<RedisToken> findAll();
}
