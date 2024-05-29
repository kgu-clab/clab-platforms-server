package page.clab.api.global.exception;

public class InvalidColumnException extends Exception {

    private static final String DEFAULT_MESSAGE = "";

    public InvalidColumnException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidColumnException(String s) {
        super(s);
    }

}