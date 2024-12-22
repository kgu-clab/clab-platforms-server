package page.clab.api.domain.auth.blacklistIp.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface ResetBlacklistIpsUseCase {

    List<String> resetBlacklistIps(HttpServletRequest request);
}
