package page.clab.api.domain.book.exception;

public class BookAlreadyReturnedException extends RuntimeException {

    public BookAlreadyReturnedException(String message) {
        super(message);
    }

}
