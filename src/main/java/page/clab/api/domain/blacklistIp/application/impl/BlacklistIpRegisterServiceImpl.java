package page.clab.api.domain.blacklistIp.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.BlacklistIpRegisterService;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BlacklistIpRegisterServiceImpl implements BlacklistIpRegisterService {

    private final SlackService slackService;
    private final BlacklistIpRepository blacklistIpRepository;

    @Transactional
    @Override
    public String register(HttpServletRequest request, BlacklistIpRequestDto requestDto) {
        String ipAddress = requestDto.getIpAddress();
        return blacklistIpRepository.findByIpAddress(ipAddress)
                .map(BlacklistIp::getIpAddress)
                .orElseGet(() -> {
                    BlacklistIp blacklistIp = BlacklistIpRequestDto.toEntity(requestDto);
                    blacklistIpRepository.save(blacklistIp);
                    slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, "Added IP: " + ipAddress);
                    return ipAddress;
                });
    }
}