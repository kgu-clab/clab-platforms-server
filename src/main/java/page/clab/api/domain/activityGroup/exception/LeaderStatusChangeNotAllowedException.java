package page.clab.api.domain.activityGroup.exception;

public class LeaderStatusChangeNotAllowedException extends RuntimeException {

    public LeaderStatusChangeNotAllowedException(String message) {
        super(message);
    }

}