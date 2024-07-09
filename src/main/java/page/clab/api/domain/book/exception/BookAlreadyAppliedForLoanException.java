package page.clab.api.domain.book.exception;

public class BookAlreadyAppliedForLoanException extends RuntimeException {
    public BookAlreadyAppliedForLoanException(String message) {
        super(message);
    }
}
