package page.clab.api.domain.memberManagement.notification.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
}
