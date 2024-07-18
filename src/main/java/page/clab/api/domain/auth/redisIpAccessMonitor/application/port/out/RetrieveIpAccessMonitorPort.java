package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out;

import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;

import java.util.List;
import java.util.Optional;

public interface RetrieveIpAccessMonitorPort {

    Optional<RedisIpAccessMonitor> findById(String ipAddress);

    List<RedisIpAccessMonitor> findAll();
}
