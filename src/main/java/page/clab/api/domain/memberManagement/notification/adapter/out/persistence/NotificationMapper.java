package page.clab.api.domain.memberManagement.notification.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

@Component
public class NotificationMapper {

    public NotificationJpaEntity toJpaEntity(Notification notification) {
        return NotificationJpaEntity.builder()
                .id(notification.getId())
                .memberId(notification.getMemberId())
                .content(notification.getContent())
                .isDeleted(notification.isDeleted())
                .build();
    }

    public Notification toDomainEntity(NotificationJpaEntity jpaEntity) {
        return Notification.builder()
                .id(jpaEntity.getId())
                .memberId(jpaEntity.getMemberId())
                .content(jpaEntity.getContent())
                .isDeleted(jpaEntity.isDeleted())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
