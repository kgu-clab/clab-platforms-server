package page.clab.api.global.exception;

public class SortingArgumentException extends Exception {

    private static final String DEFAULT_MESSAGE = "정렬 기준과 정렬 순서의 개수가 일치하지 않습니다.";

    public SortingArgumentException() {
        super(DEFAULT_MESSAGE);
    }

    public SortingArgumentException(String s) {
        super(s);
    }

}