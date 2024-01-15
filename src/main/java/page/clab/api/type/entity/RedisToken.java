package page.clab.api.type.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import page.clab.api.type.etc.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 14)
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