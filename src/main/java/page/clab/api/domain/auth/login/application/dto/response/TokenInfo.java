package page.clab.api.domain.auth.login.application.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenInfo {

    private String accessToken;
    private String refreshToken;

    public static TokenInfo create(String accessToken, String refreshToken) {
        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
