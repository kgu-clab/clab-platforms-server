package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;

public interface BlacklistIpRegisterService {
    String register(HttpServletRequest request, BlacklistIpRequestDto requestDto);
}
