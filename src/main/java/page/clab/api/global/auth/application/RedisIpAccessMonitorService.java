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

import java.time.LocalDateTime;
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

    public void registerLoginAttempt(HttpServletRequest request, String ipAddress) {
        RedisIpAccessMonitor existingAttempt = redisIpAccessMonitorRepository.findById(ipAddress).orElse(null);
        if (existingAttempt != null) {
            existingAttempt.setAttempts(existingAttempt.getAttempts() + 1);
            existingAttempt.setLastAttempt(LocalDateTime.now());
        } else {
            existingAttempt = RedisIpAccessMonitor.builder()
                    .ipAddress(ipAddress)
                    .attempts(1)
                    .lastAttempt(LocalDateTime.now())
                    .build();
        }
        if (existingAttempt.getAttempts() >= maxAttempts) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_BLOCKED, "Blocked IP: " + ipAddress);
        }
        redisIpAccessMonitorRepository.save(existingAttempt);
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
                .filter(monitor -> monitor.getAttempts() >= maxAttempts)
                .sorted(Comparator.comparing(RedisIpAccessMonitor::getLastAttempt).reversed())
                .toList();
        return new PagedResponseDto<>(filteredMonitors, pageable, filteredMonitors.size());
    }


    public String deleteAbnormalAccessIp(HttpServletRequest request, String ipAddress) {
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED, "Deleted IP: " + ipAddress);
        return redisIpAccessMonitorRepository.findById(ipAddress)
                .map(redisIpAccessMonitor -> {
                    redisIpAccessMonitorRepository.delete(redisIpAccessMonitor);
                    return redisIpAccessMonitor.getIpAddress();
                })
                .orElse(null);
    }

    public void clearAbnormalAccessIps(HttpServletRequest request) {
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_DELETED, "Deleted IP: ALL");
        redisIpAccessMonitorRepository.deleteAll();
    }

}
