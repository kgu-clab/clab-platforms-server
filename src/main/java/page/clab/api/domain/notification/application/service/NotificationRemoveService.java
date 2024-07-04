package page.clab.api.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.port.in.RemoveNotificationUseCase;
import page.clab.api.domain.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.notification.application.port.out.RetrieveNotificationPort;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class NotificationRemoveService implements RemoveNotificationUseCase {

    private final RetrieveNotificationPort retrieveNotificationPort;
    private final RegisterNotificationPort registerNotificationPort;

    @Transactional
    @Override
    public Long removeNotification(Long notificationId) throws PermissionDeniedException {
        Notification notification = retrieveNotificationPort.findByIdOrThrow(notificationId);
        notification.validateAccessPermission(notification.getMemberId());
        notification.delete();
        return registerNotificationPort.save(notification).getId();
    }
}
