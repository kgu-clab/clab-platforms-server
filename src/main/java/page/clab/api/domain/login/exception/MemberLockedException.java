package page.clab.api.domain.login.exception;

public class MemberLockedException extends Exception {

    private static final String DEFAULT_MESSAGE = "정책에 의해 계정이 일시적으로 잠겼습니다.";

    public MemberLockedException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberLockedException(String s) {
        super(s);
    }

}