package page.clab.api.domain.sharedAccount.exception;

public class SharedAccountInUseException extends RuntimeException {

    public SharedAccountInUseException(String message) {
        super(message);
    }

}