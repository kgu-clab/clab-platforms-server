package page.clab.api.global.common.notificationSetting.application.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import page.clab.api.global.common.notificationSetting.domain.AlertType;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final String webhookUrl;
    private final AlertType alertType;
    private final HttpServletRequest request;
    private final Object additionalData;

    public NotificationEvent(Object source, String webhookUrl, AlertType alertType, HttpServletRequest request,
                             Object additionalData) {
        super(source);
        this.webhookUrl = webhookUrl;
        this.alertType = alertType;
        this.request = request;
        this.additionalData = additionalData;
    }
}
