package page.clab.api.global.auth.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.global.auth.domain.RedisIpAttempt;

@Repository
public interface RedisIpAttemptRepository extends CrudRepository<RedisIpAttempt, String> {

}
