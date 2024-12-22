package page.clab.api.global.common.notificationSetting.application.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.domain.GeneralAlertType;

@Component
public class ApplicationStartupListener {

    private final ApplicationEventPublisher eventPublisher;

    public ApplicationStartupListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        eventPublisher.publishEvent(
            new NotificationEvent(this, GeneralAlertType.SERVER_START, null, null));
    }
}
