package page.clab.api.exception;

public class AssociatedAccountExistsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 존재하는 아이디입니다.";

    public AssociatedAccountExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public AssociatedAccountExistsException(String s) {
        super(s);
    }

}
