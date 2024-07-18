package page.clab.api.external.auth.redisIpAccessMonitor.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RegisterIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RetrieveIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;
import page.clab.api.external.auth.redisIpAccessMonitor.application.port.ExternalRegisterIpAccessMonitorUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class ExternalIpAccessMonitorRegisterService implements ExternalRegisterIpAccessMonitorUseCase {

    private final RegisterIpAccessMonitorPort registerIpAccessMonitorPort;
    private final RetrieveIpAccessMonitorPort retrieveIpAccessMonitorPort;
    private final SlackService slackService;

    @Value("${security.ip-attempt.max-attempts}")
    private int maxAttempts;

    @Override
    @Transactional
    public void registerIpAccessMonitor(HttpServletRequest request, String ipAddress) {
        RedisIpAccessMonitor redisIpAccessMonitor = getOrCreateRedisIpAccessMonitor(ipAddress);
        if (redisIpAccessMonitor.isBlocked()) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS_IP_BLOCKED, "Blocked IP: " + ipAddress);
        }
        registerIpAccessMonitorPort.save(redisIpAccessMonitor);
    }

    @NotNull
    private RedisIpAccessMonitor getOrCreateRedisIpAccessMonitor(String ipAddress) {
        return retrieveIpAccessMonitorPort.findById(ipAddress)
                .map(attempt -> {
                    attempt.increaseAttempts();
                    attempt.updateLastAttempt();
                    return attempt;
                })
                .orElseGet(() -> RedisIpAccessMonitor.create(ipAddress, maxAttempts));
    }
}
