package page.clab.api.domain.memberManagement.notification.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationJpaEntity toJpaEntity(Notification notification);

    Notification toDomainEntity(NotificationJpaEntity jpaEntity);
}
