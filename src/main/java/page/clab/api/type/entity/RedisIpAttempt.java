package page.clab.api.type.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "ipAttempt")
public class RedisIpAttempt {

    @Id
    private String ipAddress;

    @Indexed
    @Column(nullable = false)
    private int attempts;
    
    @Column(nullable = false)
    private LocalDateTime lastAttempt;

}