package page.clab.api.exception;

public class SearchResultNotExistException extends NullPointerException {

    private static final String DEFAULT_MESSAGE = "Search Result Not Exist!";

    public SearchResultNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public SearchResultNotExistException(String s) {
        super(s);
    }

}