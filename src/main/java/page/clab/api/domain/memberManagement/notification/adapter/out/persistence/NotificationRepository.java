package page.clab.api.domain.memberManagement.notification.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {

    Page<NotificationJpaEntity> findByMemberId(String memberId, Pageable pageable);

    List<NotificationJpaEntity> findByMemberId(String memberId);
}
