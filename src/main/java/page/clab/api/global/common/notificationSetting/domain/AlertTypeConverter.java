package page.clab.api.global.common.notificationSetting.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashMap;
import java.util.Map;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Converter(autoApply = true)
public class AlertTypeConverter implements AttributeConverter<AlertType, String> {

    private static final Map<String, AlertType> CACHE = new HashMap<>();

    static {
        for (GeneralAlertType type : GeneralAlertType.values()) {
            CACHE.put(type.getTitle(), type);
        }
        for (SecurityAlertType type : SecurityAlertType.values()) {
            CACHE.put(type.getTitle(), type);
        }
        for (ExecutivesAlertType type : ExecutivesAlertType.values()) {
            CACHE.put(type.getTitle(), type);
        }
    }

    @Override
    public String convertToDatabaseColumn(AlertType alertType) {
        if (alertType == null) {
            return null;
        }
        return alertType.getTitle();
    }

    @Override
    public AlertType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        AlertType alertType = CACHE.get(dbData);
        if (alertType == null) {
            throw new BaseException(ErrorCode.ALERT_TYPE_NOT_FOUND, "알림 타입을 찾을 수 없습니다: " + dbData);
        }
        return alertType;
    }
}
