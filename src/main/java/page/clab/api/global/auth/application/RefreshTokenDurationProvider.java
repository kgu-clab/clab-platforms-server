package page.clab.api.global.auth.application;

import java.util.Map;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.config.JwtTokenProperties;

@Component
public class RefreshTokenDurationProvider {

    private final Map<String, Long> refreshTokenDurationMap;

    public RefreshTokenDurationProvider(JwtTokenProperties jwtTokenProperties) {
        this.refreshTokenDurationMap = jwtTokenProperties.getRefreshTokenDuration();
    }

    /**
     * 사용자 역할에 따라 리프레시 토큰 유효 기간을 반환합니다.
     *
     * @param role 사용자 역할
     * @return 리프레시 토큰 유효 기간 (밀리초)
     */
    public long getRefreshTokenDuration(Role role) {
        if (role == null) {
            role = Role.GUEST;
        }
        Long duration = refreshTokenDurationMap.get(role.name());
        if (duration == null) {
            // 권한에 해당하는 설정이 없으면 기본값을 GUEST로 설정
            duration = refreshTokenDurationMap.getOrDefault(Role.GUEST.name(), 0L);
        }
        return duration;
    }
}
