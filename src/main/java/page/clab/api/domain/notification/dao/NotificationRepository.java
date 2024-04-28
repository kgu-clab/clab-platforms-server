package page.clab.api.domain.notification.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    @Query(value = "SELECT n.* FROM notification n WHERE n.is_deleted = true", nativeQuery = true)
    Page<Notification> findAllByIsDeletedTrue(Pageable pageable);

}
