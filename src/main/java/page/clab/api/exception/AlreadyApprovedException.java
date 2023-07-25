package page.clab.api.exception;

public class AlreadyApprovedException extends RuntimeException {

    public AlreadyApprovedException(String message) {
        super(message);
    }

}