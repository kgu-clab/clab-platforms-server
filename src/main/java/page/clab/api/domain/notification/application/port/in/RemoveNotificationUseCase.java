package page.clab.api.domain.notification.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveNotificationUseCase {
    Long remove(Long notificationId) throws PermissionDeniedException;
}
