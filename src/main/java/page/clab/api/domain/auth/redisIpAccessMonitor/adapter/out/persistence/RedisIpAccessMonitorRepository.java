package page.clab.api.domain.auth.redisIpAccessMonitor.adapter.out.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;

@Repository
public interface RedisIpAccessMonitorRepository extends CrudRepository<RedisIpAccessMonitor, String> {

}
