package page.clab.api.domain.notification.application.port.out;

import page.clab.api.domain.notification.domain.Notification;

import java.util.Optional;

public interface LoadNotificationPort {
    Optional<Notification> findById(Long id);
    Notification findByIdOrThrow(Long id);
}
