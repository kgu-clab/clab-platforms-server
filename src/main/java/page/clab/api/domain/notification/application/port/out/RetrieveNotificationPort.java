package page.clab.api.domain.notification.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.notification.domain.Notification;

import java.util.Optional;

public interface RetrieveNotificationPort {
    Optional<Notification> findById(Long id);
    Notification findByIdOrThrow(Long id);
    Page<Notification> findByMemberId(String memberId, Pageable pageable);
    Page<Notification> findAllByIsDeletedTrue(Pageable pageable);
}
