package page.clab.api.global.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.global.auth.dao.RedisIpAccessMonitorRepository;
import page.clab.api.global.auth.domain.RedisIpAccessMonitor;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RedisIpAccessMonitorService {

    private final SlackService slackService;

    private final RedisIpAccessMonitorRepository redisIpAccessMonitorRepository;

    @Value("${security.ip-attempt.max-attempts}")
    private int maxAttempts;

    public void registerIpAccessMonitor(HttpServletRequest request, String ipAddress) {
        RedisIpAccessMonitor redisIpAccessMonitor = redisIpAccessMonitorRepository.findById(ipAddress)
                .map(attempt -> {
                    attempt.increaseAttempts();
                    attempt.updateLastAttempt();
                    return attempt;
                })
                .orElseGet(() -> RedisIpAccessMonitor.create(ipAddress));
        if (redisIpAccessMonitor.isBlocked(maxAttempts)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_BLOCKED, "Blocked IP: " + ipAddress);
        }
        redisIpAccessMonitorRepository.save(redisIpAccessMonitor);
    }

    public boolean isBlocked(String ipAddress) {
        RedisIpAccessMonitor existingAttempt = redisIpAccessMonitorRepository.findById(ipAddress).orElse(null);
        return existingAttempt != null && existingAttempt.getAttempts() >= maxAttempts;
    }

    public PagedResponseDto<RedisIpAccessMonitor> getAbnormalAccessIps(Pageable pageable) {
        List<RedisIpAccessMonitor> allMonitors = StreamSupport
                .stream(redisIpAccessMonitorRepository.findAll().spliterator(), false)
                .toList();
        List<RedisIpAccessMonitor> filteredMonitors = allMonitors.stream()
                .filter(monitor -> monitor.isBlocked(maxAttempts))
                .sorted(Comparator.comparing(RedisIpAccessMonitor::getLastAttempt).reversed())
                .toList();
        return new PagedResponseDto<>(filteredMonitors, pageable, filteredMonitors.size());
    }


    public String deleteAbnormalAccessIp(HttpServletRequest request, String ipAddress) {
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED, "Deleted IP: " + ipAddress);
        redisIpAccessMonitorRepository.findById(ipAddress)
                .ifPresent(monitor -> {
                    redisIpAccessMonitorRepository.delete(monitor);
                    slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED, "Deleted IP: " + ipAddress);
                });
        return ipAddress;
    }

    public void clearAbnormalAccessIps(HttpServletRequest request) {
        redisIpAccessMonitorRepository.deleteAll();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED, "Deleted IP: ALL");
    }

}
