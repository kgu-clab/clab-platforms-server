package page.clab.api.domain.library.book.application.exception;

public class InvalidBorrowerException extends RuntimeException {

    public InvalidBorrowerException(String message) {
        super(message);
    }
}
