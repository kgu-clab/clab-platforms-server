package page.clab.api.global.auth.exception;

public class TokenForgeryException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "토큰이 변조되었습니다.";

    public TokenForgeryException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenForgeryException(String s) {
        super(s);
    }

}
