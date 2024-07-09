package page.clab.api.domain.book.application.exception;

public class MaxBorrowLimitExceededException extends RuntimeException {

    public MaxBorrowLimitExceededException(String message) {
        super(message);
    }
}
