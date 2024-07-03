package page.clab.api.domain.blacklistIp.application.port.in;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ResetBlacklistUseCase {
    List<String> reset(HttpServletRequest request);
}
