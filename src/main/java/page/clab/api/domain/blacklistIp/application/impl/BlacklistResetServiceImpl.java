package page.clab.api.domain.blacklistIp.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.BlacklistResetService;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlacklistResetServiceImpl implements BlacklistResetService {

    private final BlacklistIpRepository blacklistIpRepository;
    private final SlackService slackService;

    @Transactional
    @Override
    public List<String> reset(HttpServletRequest request) {
        List<String> blacklistedIps = blacklistIpRepository.findAll()
                .stream()
                .map(BlacklistIp::getIpAddress)
                .toList();
        blacklistIpRepository.deleteAll();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED, "Deleted IP: ALL");
        return blacklistedIps;
    }
}