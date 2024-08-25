package page.clab.api.domain.activity.activitygroup.exception;

public class AlreadySubmittedThisWeekAssignmentException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "해당 주차의 과제물이 이미 제출되었습니다.";

    public AlreadySubmittedThisWeekAssignmentException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadySubmittedThisWeekAssignmentException(String message) {
        super(message);
    }
}
