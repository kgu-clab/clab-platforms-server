package page.clab.api.exception;

public class UserLockedException extends Exception {

    private static final String DEFAULT_MESSAGE = "운영상의 이유로 계정이 일시적으로 잠겼습니다.";

    public UserLockedException() {
        super(DEFAULT_MESSAGE);
    }

    public UserLockedException(String s) {
        super(s);
    }

}