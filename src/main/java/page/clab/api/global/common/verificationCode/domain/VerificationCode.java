package page.clab.api.global.common.verificationCode.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "verification-code", timeToLive = 60*3)
public class VerificationCode {

    @Id
    @Column(name = "member_id")
    private String id;

    @Indexed
    private String verificationCode;
    
}