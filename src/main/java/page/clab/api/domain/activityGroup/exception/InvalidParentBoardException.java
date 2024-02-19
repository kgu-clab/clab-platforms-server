package page.clab.api.domain.activityGroup.exception;

public class InvalidParentBoardException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "올바르지 않은 부모 게시판입니다.";

    public InvalidParentBoardException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidParentBoardException(String s) {
        super(s);
    }

}