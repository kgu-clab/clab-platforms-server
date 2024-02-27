package page.clab.api.global.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import page.clab.api.global.auth.dao.RedisIpAccessMonitorRepository;
import page.clab.api.global.auth.domain.RedisIpAccessMonitor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedisIpAccessMonitorService {

    private final RedisIpAccessMonitorRepository redisIpAccessMonitorRepository;

    @Value("${security.ip-attempt.max-attempts}")
    private int maxAttempts;

    public void registerLoginAttempt(String ipAddress) {
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
        redisIpAccessMonitorRepository.save(existingAttempt);
    }

    public boolean isBlocked(String ipAddress) {
        RedisIpAccessMonitor existingAttempt = redisIpAccessMonitorRepository.findById(ipAddress).orElse(null);
        return existingAttempt != null && existingAttempt.getAttempts() >= maxAttempts;
    }

}
