package page.clab.api.global.exception;

public class SecretKeyCreationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "SecretKey 생성 중 오류가 발생했습니다.";

    public SecretKeyCreationException() {
        super(DEFAULT_MESSAGE);
    }

    public SecretKeyCreationException(String s) {
        super(s);
    }

}
