package page.clab.api.domain.auth.login.application.dto.response;

import lombok.Getter;

@Getter
public class LoginResult {

    private final String header;
    private final String refreshToken;
    private final boolean body;

    private LoginResult(String header, String refreshToken, boolean body) {
        this.header = header;
        this.refreshToken = refreshToken;
        this.body = body;
    }

    public static LoginResult create(String header, boolean body) {
        return new LoginResult(header, null, body);
    }

    public static LoginResult create(String header, String refreshToken, boolean body) {
        return new LoginResult(header, refreshToken, body);
    }
}
