package page.clab.api.domain.auth.login.application.port.out;

import page.clab.api.domain.auth.login.domain.RedisToken;

public interface RegisterRedisTokenPort {
    RedisToken save(RedisToken redisToken);
}
