package page.clab.api.domain.blacklistIp.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.port.in.ResetBlacklistIpsUseCase;
import page.clab.api.domain.blacklistIp.application.port.out.RemoveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlacklistIpResetService implements ResetBlacklistIpsUseCase {

    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;
    private final RemoveBlacklistIpPort removeBlacklistIpPort;
    private final SlackService slackService;

    @Transactional
    @Override
    public List<String> resetBlacklistIps(HttpServletRequest request) {
        List<String> blacklistedIps = retrieveBlacklistIpPort.findAll()
                .stream()
                .map(BlacklistIp::getIpAddress)
                .toList();
        removeBlacklistIpPort.deleteAll();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED, "Deleted IP: ALL");
        return blacklistedIps;
    }
}
