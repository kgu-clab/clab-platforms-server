package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.RedisToken;

import java.util.List;

public interface RetrieveAllRedisTokensPort {
    List<RedisToken> findAll();
}
