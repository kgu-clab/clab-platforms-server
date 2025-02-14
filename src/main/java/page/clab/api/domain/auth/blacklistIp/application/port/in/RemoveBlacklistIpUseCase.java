package page.clab.api.domain.auth.blacklistIp.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface RemoveBlacklistIpUseCase {

    String removeBlacklistIp(HttpServletRequest request, String ipAddress);
}
