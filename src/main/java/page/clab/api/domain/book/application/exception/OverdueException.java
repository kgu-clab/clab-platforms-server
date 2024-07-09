package page.clab.api.domain.book.application.exception;

public class OverdueException extends RuntimeException {

    public OverdueException(String message) {
        super(message);
    }
}
