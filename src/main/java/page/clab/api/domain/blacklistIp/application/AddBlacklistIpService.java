package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;

public interface AddBlacklistIpService {
    String execute(HttpServletRequest request, BlacklistIpRequestDto requestDto);
}
