package page.clab.api.domain.auth.redisIpAccessMonitor.domain;

import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "ipAccessMonitor", timeToLive = 60 * 5)
public class RedisIpAccessMonitor {

    @Id
    private String ipAddress;

    @Indexed
    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private LocalDateTime lastAttempt;

    @Column(nullable = false)
    private int maxAttempts;

    private RedisIpAccessMonitor(String ipAddress, int maxAttempts) {
        this.ipAddress = ipAddress;
        this.attempts = 1;
        this.lastAttempt = LocalDateTime.now();
        this.maxAttempts = maxAttempts;
    }

    public static RedisIpAccessMonitor create(String ipAddress, int maxAttempts) {
        return new RedisIpAccessMonitor(ipAddress, maxAttempts);
    }

    public void increaseAttempts() {
        this.attempts++;
    }

    public void updateLastAttempt() {
        this.lastAttempt = LocalDateTime.now();
    }

    public boolean isBlocked() {
        return this.attempts >= this.maxAttempts;
    }
}
