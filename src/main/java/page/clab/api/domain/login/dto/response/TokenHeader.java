package page.clab.api.domain.login.dto.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.auth.domain.ClabAuthResponseStatus;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TokenHeader {

    private int status;

    private String accessToken;

    private String refreshToken;

    private TokenHeader(int status, String accessToken, String refreshToken) {
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenHeader create(ClabAuthResponseStatus status, TokenInfo tokenInfo) {
        return new TokenHeader(status.getHttpStatus(), tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
    }

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

}
