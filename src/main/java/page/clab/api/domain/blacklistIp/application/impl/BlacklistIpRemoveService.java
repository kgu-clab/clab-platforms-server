package page.clab.api.domain.blacklistIp.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.BlacklistIpRemoveUseCase;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BlacklistIpRemoveService implements BlacklistIpRemoveUseCase {

    private final BlacklistIpRepository blacklistIpRepository;
    private final SlackService slackService;

    @Transactional
    @Override
    public String remove(HttpServletRequest request, String ipAddress) {
        BlacklistIp blacklistIp = getBlacklistIpByIpAddressOrThrow(ipAddress);
        blacklistIpRepository.delete(blacklistIp);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED, "Deleted IP: " + ipAddress);
        return blacklistIp.getIpAddress();
    }

    private BlacklistIp getBlacklistIpByIpAddressOrThrow(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress).
                orElseThrow(() -> new NotFoundException("해당 IP 주소를 찾을 수 없습니다."));
    }
}