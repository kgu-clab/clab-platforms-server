package page.clab.api.domain.notification.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.notification.domain.Notification;

public interface RetrieveDeletedNotificationsPort {
    Page<Notification> findAllByIsDeletedTrue(Pageable pageable);
}
