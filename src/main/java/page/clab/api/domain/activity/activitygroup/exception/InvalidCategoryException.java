package page.clab.api.domain.activity.activitygroup.exception;

public class InvalidCategoryException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "유효하지 않은 카테고리입니다.";

    public InvalidCategoryException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidCategoryException(String s) {
        super(s);
    }

}
