package page.clab.api.domain.book.application.exception;

public class BookAlreadyAppliedForLoanException extends RuntimeException {
    public BookAlreadyAppliedForLoanException(String message) {
        super(message);
    }
}
