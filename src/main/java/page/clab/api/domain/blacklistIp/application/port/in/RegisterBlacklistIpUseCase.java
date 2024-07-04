package page.clab.api.domain.blacklistIp.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;

public interface RegisterBlacklistIpUseCase {
    String registerBlacklistIp(HttpServletRequest request, BlacklistIpRequestDto requestDto);
}
