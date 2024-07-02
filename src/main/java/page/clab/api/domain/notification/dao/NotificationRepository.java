package page.clab.api.domain.notification.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.domain.notification.domain.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByMemberId(String memberId, Pageable pageable);
    List<Notification> findByMemberId(String memberId);
    @Query(value = "SELECT n.* FROM notification n WHERE n.is_deleted = true", nativeQuery = true)
    Page<Notification> findAllByIsDeletedTrue(Pageable pageable);
}
