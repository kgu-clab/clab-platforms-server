package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;

public interface BlacklistIpRemoveService {
    String remove(HttpServletRequest request, String ipAddress);
}
