package page.clab.api.exception;

public class BookAlreadyBorrowedException extends RuntimeException {

    public BookAlreadyBorrowedException(String message) {
        super(message);
    }

}
