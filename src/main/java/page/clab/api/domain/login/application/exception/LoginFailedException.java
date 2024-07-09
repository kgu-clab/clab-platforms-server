package page.clab.api.domain.login.application.exception;

public class LoginFailedException extends Exception {

    private static final String DEFAULT_MESSAGE = "로그인 인증 정보가 올바르지 않습니다.";

    public LoginFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public LoginFailedException(String s) {
        super(s);
    }
}
