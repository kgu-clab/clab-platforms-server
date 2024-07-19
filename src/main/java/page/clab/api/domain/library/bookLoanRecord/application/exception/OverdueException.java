package page.clab.api.domain.library.bookLoanRecord.application.exception;

public class OverdueException extends RuntimeException {

    public OverdueException(String message) {
        super(message);
    }
}
