package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.port.in.BlacklistResetUseCase;
import page.clab.api.domain.blacklistIp.application.port.out.LoadBlacklistIpsPort;
import page.clab.api.domain.blacklistIp.application.port.out.RemoveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlacklistResetService implements BlacklistResetUseCase {

    private final LoadBlacklistIpsPort loadBlacklistIpsPort;
    private final RemoveBlacklistIpPort removeBlacklistIpPort;
    private final SlackService slackService;

    @Transactional
    @Override
    public List<String> reset(HttpServletRequest request) {
        List<String> blacklistedIps = loadBlacklistIpsPort.findAll()
                .stream()
                .map(BlacklistIp::getIpAddress)
                .toList();
        removeBlacklistIpPort.deleteAll();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED, "Deleted IP: ALL");
        return blacklistedIps;
    }
}
