package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;

public interface RemoveBlacklistIpService {
    String execute(HttpServletRequest request, String ipAddress);
}
