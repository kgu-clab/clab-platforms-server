package page.clab.api.global.exception;

public class SearchResultNotExistException extends NullPointerException {

    private static final String DEFAULT_MESSAGE = "검색 결과가 존재하지 않습니다.";

    public SearchResultNotExistException() {
        super(DEFAULT_MESSAGE);
    }

    public SearchResultNotExistException(String s) {
        super(s);
    }

}