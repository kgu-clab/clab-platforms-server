package page.clab.api.global.auth.exception;

public class AuthenticationInfoNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "No authentication information available.";

    public AuthenticationInfoNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthenticationInfoNotFoundException(String s) {
        super(s);
    }

}
