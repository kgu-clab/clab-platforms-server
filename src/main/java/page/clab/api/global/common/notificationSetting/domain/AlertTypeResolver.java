package page.clab.api.global.common.notificationSetting.domain;

import org.springframework.stereotype.Service;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

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
        throw new BaseException(ErrorCode.ALERT_TYPE_NOT_FOUND, "알림 타입을 찾을 수 없습니다: " + alertTypeName);
    }
}
