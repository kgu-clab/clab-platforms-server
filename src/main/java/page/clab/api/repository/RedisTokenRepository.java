package page.clab.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.RedisToken;

import java.util.Optional;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {

    Optional<RedisToken> findByAccessToken(String accessToken);

    Optional<RedisToken> findByRefreshToken(String refreshToken);

}
