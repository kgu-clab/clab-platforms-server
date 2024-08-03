package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface RemoveAbnormalAccessIpUseCase {
    String removeAbnormalAccessIp(HttpServletRequest request, String ipAddress);
}
