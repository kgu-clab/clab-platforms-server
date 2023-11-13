package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import javax.persistence.Id;

@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken", timeToLive = 60*40)
public class RedisToken {

    @Id
    @Column(name = "member_id")
    private String id;

    @Indexed
    private String accessToken;

    private String refreshToken;

}