package page.clab.api.domain.auth.login.domain;

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
import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.memberManagement.member.domain.Role;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 14)
public class RedisToken {

    @Id
    private UUID id;
    private String memberId;
    private Role role;
    private String ip;

    @Indexed
    private String accessToken;

    @Indexed
    private String refreshToken;

    public static RedisToken create(String memberId, Role role, String ip, TokenInfo tokenInfo) {
        return RedisToken.builder()
                .id(UUID.randomUUID())
                .memberId(memberId)
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
