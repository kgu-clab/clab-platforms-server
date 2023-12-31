package page.clab.api.exception;

public class DuplicateLoginException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "중복 로그인이 감지되었습니다.";

    public DuplicateLoginException() {
        super(DEFAULT_MESSAGE);
    }

    public DuplicateLoginException(String s) {
        super(s);
    }

}
