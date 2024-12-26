package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;

public interface ClearAbnormalAccessIpsUseCase {

    List<RedisIpAccessMonitor> clearAbnormalAccessIps(HttpServletRequest request);
}
