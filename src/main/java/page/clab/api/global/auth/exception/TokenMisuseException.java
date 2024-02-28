package page.clab.api.global.auth.exception;

public class TokenMisuseException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "토큰이 잘못 사용되었습니다.";

    public TokenMisuseException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenMisuseException(String s) {
        super(s);
    }

}
