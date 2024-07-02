package page.clab.api.domain.notification.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface NotificationRemoveUseCase {
    Long remove(Long notificationId) throws PermissionDeniedException;
}

