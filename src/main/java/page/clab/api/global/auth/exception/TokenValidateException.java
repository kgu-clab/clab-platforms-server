package page.clab.api.global.auth.exception;

public class TokenValidateException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "토큰이 유효하지 않습니다.";

    public TokenValidateException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenValidateException(String s) {
        super(s);
    }

}
