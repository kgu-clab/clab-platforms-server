package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "ipAttempt", timeToLive = 60*5)
public class RedisIpAttempt {

    @Id
    private String ipAddress;

    @Indexed
    @Column(nullable = false)
    private int attempts;
    
    @Column(nullable = false)
    private LocalDateTime lastAttempt;

}