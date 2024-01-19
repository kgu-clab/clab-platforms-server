package page.clab.api.domain.notification.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    public static NotificationResponseDto of(Notification notification) {
        return ModelMapperUtil.getModelMapper().map(notification, NotificationResponseDto.class);
    }

}
