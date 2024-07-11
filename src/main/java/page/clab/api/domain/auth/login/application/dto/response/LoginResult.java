package page.clab.api.domain.auth.login.application.dto.response;

import lombok.Getter;

public class LoginResult {

    @Getter
    private final String header;

    private final boolean body;

    private LoginResult(String header, boolean body) {
        this.header = header;
        this.body = body;
    }

    public static LoginResult create(String header, boolean body) {
        return new LoginResult(header, body);
    }

    public boolean getBody() {
        return body;
    }
}
