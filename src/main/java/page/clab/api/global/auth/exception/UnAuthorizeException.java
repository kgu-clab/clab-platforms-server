package page.clab.api.global.auth.exception;

public class UnAuthorizeException extends NullPointerException {

    private static final String DEFAULT_MESSAGE = "인증되지 않은 사용자입니다.";

    public UnAuthorizeException() {
        super(DEFAULT_MESSAGE);
    }

    public UnAuthorizeException(String s) {
        super(s);
    }

}
