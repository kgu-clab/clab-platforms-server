package page.clab.api.global.auth.exception;

public class TokenValidateException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Fail to validate Token";

    public TokenValidateException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenValidateException(String s) {
        super(s);
    }

}
