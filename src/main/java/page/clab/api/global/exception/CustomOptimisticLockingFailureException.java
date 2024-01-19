package page.clab.api.global.exception;

public class CustomOptimisticLockingFailureException extends Exception {

    public CustomOptimisticLockingFailureException(String message) {
        super(message);
    }

}