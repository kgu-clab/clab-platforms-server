package page.clab.api.exception;

public class PermissionDeniedException extends Exception {

    private static final String DEFAULT_MESSAGE = "Permission Denied!";

    public PermissionDeniedException() {
        super(DEFAULT_MESSAGE);
    }

    public PermissionDeniedException(String s) {
        super(s);
    }

}