package page.clab.api.global.exception;

public class EncryptionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "암호화 처리 중 오류가 발생했습니다.";

    public EncryptionException() {
        super(DEFAULT_MESSAGE);
    }

    public EncryptionException(String s) {
        super(s);
    }

}
