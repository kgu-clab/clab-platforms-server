package page.clab.api.global.common.slack.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import page.clab.api.global.common.slack.exception.AlertTypeNotFoundException;

import java.util.HashMap;
import java.util.Map;

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
            throw new AlertTypeNotFoundException(dbData);
        }
        return alertType;
    }
}
