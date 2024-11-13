package page.clab.api.global.common.notificationSetting.application.exception;

public class AlertTypeNotFoundException extends RuntimeException {

    public AlertTypeNotFoundException(String alertTypeName) {
        super("Unknown alert type: " + alertTypeName);
    }
}
