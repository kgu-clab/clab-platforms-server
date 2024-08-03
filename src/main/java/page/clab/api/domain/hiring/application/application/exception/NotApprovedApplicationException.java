package page.clab.api.domain.hiring.application.application.exception;

public class NotApprovedApplicationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "승인되지 않은 지원서입니다.";

    public NotApprovedApplicationException() {
        super(DEFAULT_MESSAGE);
    }

    public NotApprovedApplicationException(String s) {
        super(s);
    }
}
