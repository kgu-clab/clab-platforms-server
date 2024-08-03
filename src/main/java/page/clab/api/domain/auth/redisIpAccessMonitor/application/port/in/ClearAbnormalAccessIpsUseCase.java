package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;

import java.util.List;

public interface ClearAbnormalAccessIpsUseCase {
    List<RedisIpAccessMonitor> clearAbnormalAccessIps(HttpServletRequest request);
}
