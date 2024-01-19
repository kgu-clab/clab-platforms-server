package page.clab.api.global.exception;

public class ImageCompressionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미지 압축에 실패했습니다.";

    public ImageCompressionException() {
        super(DEFAULT_MESSAGE);
    }

    public ImageCompressionException(String s) {
        super(s);
    }

}
