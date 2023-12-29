package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import page.clab.api.type.etc.Role;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 60*60*24*14)
public class RedisToken {

    @Id
    @Column(name = "member_id")
    private String id;

    private Role role;

    private String ip;

    @Indexed
    private String accessToken;

    @Indexed
    private String refreshToken;

}