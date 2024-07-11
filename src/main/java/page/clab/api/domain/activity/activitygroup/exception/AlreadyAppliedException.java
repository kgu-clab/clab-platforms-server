package page.clab.api.domain.activity.activitygroup.exception;

public class AlreadyAppliedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "해당 활동에 신청한 내역이 존재합니다.";

    public AlreadyAppliedException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyAppliedException(String s) {
        super(s);
    }

}
