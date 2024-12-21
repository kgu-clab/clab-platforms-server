package page.clab.api.domain.auth.login.application.dto.response;

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
    }

    private TokenHeader(String accessToken) {
        this.accessToken = accessToken;
    }

    private TokenHeader(TokenInfo tokenInfo) {
        this.accessToken = tokenInfo.getAccessToken();
        this.refreshToken = tokenInfo.getRefreshToken();
    }

    public static TokenHeader create() {
        return new TokenHeader();
    }

    public static TokenHeader create(String accessToken) {
        return new TokenHeader(accessToken);
    }

    public static TokenHeader create(TokenInfo tokenInfo) {
        return new TokenHeader(tokenInfo);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
