package page.clab.api.global.config;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtTokenProperties {

    private String secretKey;
    private TokenValidity tokenValidity;

    public long getAccessTokenDuration() {
        return tokenValidity.getAccessTokenDuration();
    }

    public Map<String, Long> getRefreshTokenDuration() {
        return tokenValidity.getRefreshTokenDuration();
    }

    /**
     * 토큰 유효 기간을 담는 내부 클래스.
     */
    @Getter
    @Setter
    public static class TokenValidity {

        private long accessTokenDuration;
        private Map<String, Long> refreshTokenDuration; // 권한별 리프레시 토큰 유효 기간
    }
}
