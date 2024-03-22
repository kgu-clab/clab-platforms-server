package page.clab.api.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.notification.domain.Notification;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
