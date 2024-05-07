package page.clab.api.domain.login.dto.response;

public class LoginResult {

    private final String header;

    private final boolean body;

    private LoginResult(String header, boolean body) {
        this.header = header;
        this.body = body;
    }

    public static LoginResult create(String header, boolean body) {
        return new LoginResult(header, body);
    }

    public String getHeader() {
        return header;
    }

    public boolean getBody() {
        return body;
    }

}
