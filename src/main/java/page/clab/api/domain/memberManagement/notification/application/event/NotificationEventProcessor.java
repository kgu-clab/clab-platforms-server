package page.clab.api.domain.memberManagement.notification.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessor;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessorRegistry;
import page.clab.api.domain.memberManagement.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.memberManagement.notification.application.port.out.RetrieveNotificationPort;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventProcessor implements MemberEventProcessor {

    private final RetrieveNotificationPort retrieveNotificationPort;
    private final RegisterNotificationPort registerNotificationPort;
    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Notification> notifications = retrieveNotificationPort.findByMemberId(memberId);
        notifications.forEach(Notification::delete);
        registerNotificationPort.saveAll(notifications);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}
