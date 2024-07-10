package page.clab.api.domain.notification.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {
    Page<NotificationJpaEntity> findByMemberId(String memberId, Pageable pageable);

    List<NotificationJpaEntity> findByMemberId(String memberId);

    @Query(value = "SELECT n.* FROM notification n WHERE n.is_deleted = true", nativeQuery = true)
    Page<NotificationJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
