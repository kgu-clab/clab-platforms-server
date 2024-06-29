package page.clab.api.domain.notification.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteNotificationService {
    Long execute(Long notificationId) throws PermissionDeniedException;
}

