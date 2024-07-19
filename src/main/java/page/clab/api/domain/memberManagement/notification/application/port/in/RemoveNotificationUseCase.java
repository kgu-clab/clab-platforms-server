package page.clab.api.domain.memberManagement.notification.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveNotificationUseCase {
    Long removeNotification(Long notificationId) throws PermissionDeniedException;
}
