package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out;

import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;

public interface RegisterIpAccessMonitorPort {
    void save(RedisIpAccessMonitor redisIpAccessMonitor);
}
