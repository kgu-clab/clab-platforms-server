package page.clab.api.domain.activity.exception;

public class LeaderStatusChangeNotAllowedException extends RuntimeException {

    public LeaderStatusChangeNotAllowedException(String message) {
        super(message);
    }

}
