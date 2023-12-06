package page.clab.api.repository;

import org.springframework.data.repository.CrudRepository;
import page.clab.api.type.entity.RedisIpAttempt;

public interface RedisIpAttemptRepository extends CrudRepository<RedisIpAttempt, String> {

}
