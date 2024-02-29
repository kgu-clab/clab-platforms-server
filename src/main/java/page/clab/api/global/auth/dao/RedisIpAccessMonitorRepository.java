package page.clab.api.global.auth.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.global.auth.domain.RedisIpAccessMonitor;

@Repository
public interface RedisIpAccessMonitorRepository extends CrudRepository<RedisIpAccessMonitor, String> {

}
