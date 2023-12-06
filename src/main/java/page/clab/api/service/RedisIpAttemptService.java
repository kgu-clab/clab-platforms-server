package page.clab.api.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import page.clab.api.type.entity.RedisIpAttempt;

@Service
public class RedisIpAttemptService {

    private final RedisTemplate<String, RedisIpAttempt> redisTemplate;

    @Value("${ip-attempt.max-attempts}")
    private int MAX_ATTEMPTS;

    @Value("${ip-attempt.lock-duration-minutes}")
    private long LOCK_DURATION_MINUTES;

    public RedisIpAttemptService(RedisTemplate<String, RedisIpAttempt> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void registerLoginAttempt(String ipAddress) {
        String key = getRedisKey(ipAddress);
        RedisIpAttempt existingAttempt = redisTemplate.opsForValue().get(key);
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
        redisTemplate.opsForValue().set(key, existingAttempt, LOCK_DURATION_MINUTES, TimeUnit.MINUTES);
    }

    public boolean isBlocked(String ipAddress) {
        RedisIpAttempt existingAttempt = redisTemplate.opsForValue().get(getRedisKey(ipAddress));
        return existingAttempt != null && existingAttempt.getAttempts() >= MAX_ATTEMPTS;
    }

    private String getRedisKey(String ipAddress) {
        return "ip-attempt:" + ipAddress;
    }

}
