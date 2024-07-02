package page.clab.api.domain.blacklistIp.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

public interface BlacklistIpRemoveUseCase {
    String remove(HttpServletRequest request, String ipAddress);
}
