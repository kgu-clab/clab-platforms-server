package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface RegisterIpAccessMonitorUseCase {
    void registerIpAccessMonitor(HttpServletRequest request, String ipAddress);
}
