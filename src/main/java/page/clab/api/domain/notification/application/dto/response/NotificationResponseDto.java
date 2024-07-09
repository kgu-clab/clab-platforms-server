package page.clab.api.domain.notification.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.notification.domain.Notification;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    public static NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
