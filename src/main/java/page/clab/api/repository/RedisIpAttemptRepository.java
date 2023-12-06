package page.clab.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.RedisIpAttempt;

@Repository
public interface RedisIpAttemptRepository extends CrudRepository<RedisIpAttempt, String> {

}
