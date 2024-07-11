package page.clab.api.domain.library.bookLoanRecord.application.exception;

public class MaxBorrowLimitExceededException extends RuntimeException {

    public MaxBorrowLimitExceededException(String message) {
        super(message);
    }
}
