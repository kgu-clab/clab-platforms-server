package page.clab.api.domain.auth.login.application.port.out;

import java.util.List;
import java.util.Optional;
import page.clab.api.domain.auth.login.domain.RedisToken;

public interface RetrieveRedisTokenPort {

    Optional<RedisToken> findByAccessToken(String accessToken);

    Optional<RedisToken> findByRefreshToken(String refreshToken);

    List<RedisToken> findAll();
}
