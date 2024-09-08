package page.clab.api.domain.activity.activitygroup.exception;

public class CurriculumLengthExceededException extends RuntimeException {

    public CurriculumLengthExceededException(String message) {
        super(message);
    }
}