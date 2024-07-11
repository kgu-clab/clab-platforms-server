package page.clab.api.domain.library.bookLoanRecord.application.exception;

public class LoanNotPendingException extends RuntimeException {

    public LoanNotPendingException(String message) {
        super(message);
    }
}
