package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.port.in.RegisterBlacklistIpUseCase;
import page.clab.api.domain.blacklistIp.application.port.out.RegisterBlacklistIpPort;
import page.clab.api.domain.blacklistIp.application.port.out.RetrieveBlacklistIpByIpAddressPort;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BlacklistIpRegisterService implements RegisterBlacklistIpUseCase {

    private final SlackService slackService;
    private final RegisterBlacklistIpPort registerBlacklistIpPort;
    private final RetrieveBlacklistIpByIpAddressPort retrieveBlacklistIpByIpAddressPort;

    @Transactional
    @Override
    public String register(HttpServletRequest request, BlacklistIpRequestDto requestDto) {
        String ipAddress = requestDto.getIpAddress();
        return retrieveBlacklistIpByIpAddressPort.findByIpAddress(ipAddress)
                .map(BlacklistIp::getIpAddress)
                .orElseGet(() -> {
                    BlacklistIp blacklistIp = BlacklistIpRequestDto.toEntity(requestDto);
                    registerBlacklistIpPort.save(blacklistIp);
                    slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, "Added IP: " + ipAddress);
                    return ipAddress;
                });
    }
}
