package page.clab.api.domain.login.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import page.clab.api.domain.login.dto.response.TokenInfo;
import page.clab.api.domain.member.domain.Role;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static RedisToken create(String id, Role role, String ip, TokenInfo tokenInfo) {
        return RedisToken.builder()
                .id(id)
                .role(role)
                .ip(ip)
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }

    public boolean isSameIp(String ip) {
        return this.ip.equals(ip);
    }

    public boolean isAdminToken() {
        return role == Role.ADMIN || role == Role.SUPER;
    }
}