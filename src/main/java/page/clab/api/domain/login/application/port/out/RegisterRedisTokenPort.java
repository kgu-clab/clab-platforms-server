package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.RedisToken;

public interface RegisterRedisTokenPort {
    RedisToken save(RedisToken redisToken);
}
