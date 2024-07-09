package page.clab.api.domain.book.application.exception;

public class LoanSuspensionException extends RuntimeException {

    public LoanSuspensionException(String message) {
        super(message);
    }
}
