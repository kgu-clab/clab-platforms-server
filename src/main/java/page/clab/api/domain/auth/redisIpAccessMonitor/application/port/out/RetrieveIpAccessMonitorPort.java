package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out;

import java.util.List;
import java.util.Optional;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;

public interface RetrieveIpAccessMonitorPort {

    Optional<RedisIpAccessMonitor> findById(String ipAddress);

    List<RedisIpAccessMonitor> findAll();
}
