package page.clab.api.domain.blacklistIp.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface RemoveBlacklistIpUseCase {
    String removeBlacklistIp(HttpServletRequest request, String ipAddress);
}
