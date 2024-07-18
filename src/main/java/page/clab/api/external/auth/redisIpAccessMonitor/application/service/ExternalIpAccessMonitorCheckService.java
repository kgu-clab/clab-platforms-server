package page.clab.api.external.auth.redisIpAccessMonitor.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.redisIpAccessMonitor.application.port.out.RetrieveIpAccessMonitorPort;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;
import page.clab.api.external.auth.redisIpAccessMonitor.application.port.ExternalCheckIpBlockedUseCase;

@Service
@RequiredArgsConstructor
public class ExternalIpAccessMonitorCheckService implements ExternalCheckIpBlockedUseCase {

    private final RetrieveIpAccessMonitorPort retrieveIpAccessMonitorPort;

    @Value("${security.ip-attempt.max-attempts}")
    private int maxAttempts;

    @Override
    @Transactional(readOnly = true)
    public boolean isIpBlocked(String ipAddress) {
        RedisIpAccessMonitor existingAttempt = retrieveIpAccessMonitorPort.findById(ipAddress).orElse(null);
        return existingAttempt != null && existingAttempt.getAttempts() >= maxAttempts;
    }
}
