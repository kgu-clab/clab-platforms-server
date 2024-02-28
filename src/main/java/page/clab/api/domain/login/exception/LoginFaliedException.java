package page.clab.api.domain.login.exception;

public class LoginFaliedException extends Exception {

    private static final String DEFAULT_MESSAGE = "로그인 인증 정보가 올바르지 않습니다.";

    public LoginFaliedException() {
        super(DEFAULT_MESSAGE);
    }

    public LoginFaliedException(String s) {
        super(s);
    }

}