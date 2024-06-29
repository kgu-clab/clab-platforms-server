package page.clab.api.domain.notification.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.domain.Notification;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventProcessor implements MemberEventProcessor {

    private final NotificationRepository notificationRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);
        notifications.forEach(Notification::delete);
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}
