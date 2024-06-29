package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ClearBlacklistService {
    List<String> execute(HttpServletRequest request);
}
