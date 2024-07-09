package page.clab.api.domain.book.exception;

public class OverdueException extends RuntimeException {

    public OverdueException(String message) {
        super(message);
    }
}
