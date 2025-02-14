package page.clab.api.domain.memberManagement.notification.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.notification.application.dto.response.NotificationResponseDto;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

@Component
public class NotificationDtoMapper {

    public NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
            .id(notification.getId())
            .content(notification.getContent())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
