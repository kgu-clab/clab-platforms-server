package page.clab.api.domain.auth.login.application.port.out;

import page.clab.api.domain.auth.login.domain.RedisToken;

public interface RemoveRedisTokenPort {

    void delete(RedisToken redisToken);

    void deleteByMemberId(String memberId);
}
