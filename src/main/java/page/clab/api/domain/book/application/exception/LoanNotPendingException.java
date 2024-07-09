package page.clab.api.domain.book.application.exception;

public class LoanNotPendingException extends RuntimeException {

    public LoanNotPendingException(String message) {
        super(message);
    }
}
