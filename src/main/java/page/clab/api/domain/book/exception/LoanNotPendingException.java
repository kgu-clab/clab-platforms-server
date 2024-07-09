package page.clab.api.domain.book.exception;

public class LoanNotPendingException extends RuntimeException {

    public LoanNotPendingException(String message) {
        super(message);
    }
}
