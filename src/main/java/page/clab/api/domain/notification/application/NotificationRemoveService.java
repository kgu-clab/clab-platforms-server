package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.port.in.NotificationRemoveUseCase;
import page.clab.api.domain.notification.application.port.out.LoadNotificationPort;
import page.clab.api.domain.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class NotificationRemoveService implements NotificationRemoveUseCase {

    private final LoadNotificationPort loadNotificationPort;
    private final RegisterNotificationPort registerNotificationPort;

    @Transactional
    @Override
    public Long remove(Long notificationId) throws PermissionDeniedException {
        Notification notification = loadNotificationPort.findByIdOrThrow(notificationId);
        notification.validateAccessPermission(notification.getMemberId());
        notification.delete();
        return registerNotificationPort.save(notification).getId();
    }
}
