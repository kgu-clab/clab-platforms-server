package page.clab.api.global.common.slack.exception;

public class AlertTypeNotFoundException extends RuntimeException {

    public AlertTypeNotFoundException(String alertTypeName) {
        super("Unknown alert type: " + alertTypeName);
    }
}
