package page.clab.api.domain.library.bookLoanRecord.application.exception;

public class BookAlreadyAppliedForLoanException extends RuntimeException {
    public BookAlreadyAppliedForLoanException(String message) {
        super(message);
    }
}
