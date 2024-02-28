package page.clab.api.global.auth.exception;

public class AuthenticationInfoNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "인증 정보가 존재하지 않습니다.";

    public AuthenticationInfoNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthenticationInfoNotFoundException(String s) {
        super(s);
    }

}
