package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BlacklistResetService {
    List<String> reset(HttpServletRequest request);
}
