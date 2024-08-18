package page.clab.api.domain.hiring.application.application.exception;

public class RecruitmentEndDateExceededException extends RuntimeException {

    public RecruitmentEndDateExceededException(String message) {
        super(message);
    }
}
