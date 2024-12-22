package page.clab.api.external.auth.redisIpAccessMonitor.application.port;

import jakarta.servlet.http.HttpServletRequest;

public interface ExternalRegisterIpAccessMonitorUseCase {

    void registerIpAccessMonitor(HttpServletRequest request, String clientIpAddress);
}
