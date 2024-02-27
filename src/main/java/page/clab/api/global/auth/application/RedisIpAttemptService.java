package page.clab.api.global.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import page.clab.api.global.auth.dao.RedisIpAttemptRepository;
import page.clab.api.global.auth.domain.RedisIpAttempt;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedisIpAttemptService {

    private final RedisIpAttemptRepository redisIpAttemptRepository;

    @Value("${security.ip-attempt.max-attempts}")
    private int MAX_ATTEMPTS;

    public void registerLoginAttempt(String ipAddress) {
        RedisIpAttempt existingAttempt = redisIpAttemptRepository.findById(ipAddress).orElse(null);
        if (existingAttempt != null) {
            existingAttempt.setAttempts(existingAttempt.getAttempts() + 1);
            existingAttempt.setLastAttempt(LocalDateTime.now());
        } else {
            existingAttempt = RedisIpAttempt.builder()
                    .ipAddress(ipAddress)
                    .attempts(1)
                    .lastAttempt(LocalDateTime.now())
                    .build();
        }
        redisIpAttemptRepository.save(existingAttempt);
    }

    public boolean isBlocked(String ipAddress) {
        RedisIpAttempt existingAttempt = redisIpAttemptRepository.findById(ipAddress).orElse(null);
        return existingAttempt != null && existingAttempt.getAttempts() >= MAX_ATTEMPTS;
    }

}
