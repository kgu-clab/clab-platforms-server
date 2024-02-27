package page.clab.api.global.exception;

public class DecryptionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "복호화 처리 중 오류가 발생했습니다.";

    public DecryptionException() {
        super(DEFAULT_MESSAGE);
    }

    public DecryptionException(String s) {
        super(s);
    }

}
