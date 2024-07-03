package page.clab.api.domain.blacklistIp.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.port.in.RemoveBlacklistIpUseCase;
import page.clab.api.domain.blacklistIp.application.port.out.RemoveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BlacklistIpRemoveService implements RemoveBlacklistIpUseCase {

    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;
    private final RemoveBlacklistIpPort removeBlacklistIpPort;
    private final SlackService slackService;

    @Transactional
    @Override
    public String remove(HttpServletRequest request, String ipAddress) {
        BlacklistIp blacklistIp = retrieveBlacklistIpPort.findByIpAddressOrThrow(ipAddress);
        removeBlacklistIpPort.delete(blacklistIp);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED, "Deleted IP: " + ipAddress);
        return blacklistIp.getIpAddress();
    }
}
