package page.clab.api.exception;

public class LoginFaliedException extends Exception {

    private static final String DEFAULT_MESSAGE = "ID 또는 PW가 일치하지 않습니다.";

    public LoginFaliedException() {
        super(DEFAULT_MESSAGE);
    }

    public LoginFaliedException(String s) {
        super(s);
    }

}