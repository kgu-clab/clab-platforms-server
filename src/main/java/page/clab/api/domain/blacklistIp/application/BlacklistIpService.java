package page.clab.api.domain.blacklistIp.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.domain.blacklistIp.dto.request.BlacklistIpRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistIpService {

    private final SlackService slackService;

    private final BlacklistIpRepository blacklistIpRepository;

    public String addBlacklistedIp(HttpServletRequest request, BlacklistIpRequestDto requestDto) {
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

    @Transactional(readOnly = true)
    public PagedResponseDto<BlacklistIp> getBlacklistedIps(Pageable pageable) {
        Page<BlacklistIp> blacklistedIps = blacklistIpRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(blacklistedIps);
    }

    @Transactional
    public String deleteBlacklistedIp(HttpServletRequest request, String ipAddress) {
        BlacklistIp blacklistIp = getBlacklistIpByIpAddressOrThrow(ipAddress);
        blacklistIpRepository.delete(blacklistIp);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED, "Deleted IP: " + ipAddress);
        return blacklistIp.getIpAddress();
    }

    public List<String> clearBlacklist(HttpServletRequest request) {
        List<String> blacklistedIps = blacklistIpRepository.findAll()
                .stream()
                .map(BlacklistIp::getIpAddress)
                .toList();
        blacklistIpRepository.deleteAll();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_REMOVED, "Deleted IP: ALL");
        return blacklistedIps;
    }

    private BlacklistIp getBlacklistIpByIpAddressOrThrow(String ipAddress) {
        return blacklistIpRepository.findByIpAddress(ipAddress).
                orElseThrow(() -> new NotFoundException("해당 IP 주소를 찾을 수 없습니다."));
    }

}


