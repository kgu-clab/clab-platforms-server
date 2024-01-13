package page.clab.api.exception;

public class CustomOptimisticLockingFailureException extends Exception {

    public CustomOptimisticLockingFailureException(String message) {
        super(message);
    }

}