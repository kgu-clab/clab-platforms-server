package page.clab.api.domain.login.application.dto.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenHeader {

    private String accessToken;
    private String refreshToken;

    private TokenHeader() {
        this.accessToken = null;
        this.refreshToken = null;
    }

    private TokenHeader(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenHeader create() {
        return new TokenHeader();
    }

    public static TokenHeader create(TokenInfo tokenInfo) {
        return new TokenHeader(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
    }

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }
}
