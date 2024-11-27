package page.clab.api.global.common.notificationSetting.domain;

import org.springframework.stereotype.Service;
import page.clab.api.global.common.notificationSetting.application.exception.AlertTypeNotFoundException;

@Service
public class AlertTypeResolver {

    public AlertType resolve(String alertTypeName) {
        for (GeneralAlertType type : GeneralAlertType.values()) {
            if (type.getTitle().equals(alertTypeName)) {
                return type;
            }
        }
        for (SecurityAlertType type : SecurityAlertType.values()) {
            if (type.getTitle().equals(alertTypeName)) {
                return type;
            }
        }
        throw new AlertTypeNotFoundException(alertTypeName);
    }
}
