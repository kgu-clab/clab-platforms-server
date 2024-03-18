package page.clab.api.global.auth.domain;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "ipAccessMonitor", timeToLive = 60 * 5)
public class RedisIpAccessMonitor {

    @Id
    private String ipAddress;

    @Indexed
    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private LocalDateTime lastAttempt;

    private RedisIpAccessMonitor(String ipAddress) {
        this.ipAddress = ipAddress;
        this.attempts = 1;
        this.lastAttempt = LocalDateTime.now();
    }

    public static RedisIpAccessMonitor create(String ipAddress) {
        return new RedisIpAccessMonitor(ipAddress);
    }

    public void increaseAttempts() {
        this.attempts++;
    }

    public void updateLastAttempt() {
        this.lastAttempt = LocalDateTime.now();
    }

    public boolean isBlocked(int maxAttempts) {
        return this.attempts >= maxAttempts;
    }

}