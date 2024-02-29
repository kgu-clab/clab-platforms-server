package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistIpService {

    private final SlackService slackService;

    private final BlacklistIpRepository blacklistIpRepository;

    public String addBlacklistedIp(HttpServletRequest request, String ipAddress) {
        BlacklistIp blacklistIp = getBlacklistIpByIpAddress(ipAddress);
        if (blacklistIp != null) {
            return blacklistIp.getIpAddress();
        }
        blacklistIp = BlacklistIp.builder().ipAddress(ipAddress).build();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, "Added IP: " + ipAddress);
        return blacklistIpRepository.save(blacklistIp).getIpAddress();
    }

    public PagedResponseDto<BlacklistIp> getBlacklistedIps(Pageable pageable) {
        Page<BlacklistIp> blacklistedIps = blacklistIpRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(blacklistedIps);
    }

    @Transactional
    public String deleteBlacklistedIp(HttpServletRequest request, String ipAddress) {
        BlacklistIp blacklistIp = getBlacklistIpByIpAddressOrThrow(ipAddress);
        blacklistIpRepository.delete(blacklistIp);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, "Deleted IP: " + ipAddress);
        return blacklistIp.getIpAddress();
    }

    public void clearBlacklist(HttpServletRequest request) {
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, "Deleted IP: ALL");
        blacklistIpRepository.deleteAll();
    }

    private BlacklistIp getBlacklistIpByIpAddressOrThrow(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress).
                orElseThrow(() -> new NotFoundException("해당 IP 주소를 찾을 수 없습니다."));
    }

    private BlacklistIp getBlacklistIpByIpAddress(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress)
                .orElse(null);
    }

}


