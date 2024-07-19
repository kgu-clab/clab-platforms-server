package page.clab.api.domain.memberManagement.notification.application.port.in;

import page.clab.api.domain.memberManagement.notification.application.dto.request.NotificationRequestDto;

public interface RegisterNotificationUseCase {
    Long registerNotification(NotificationRequestDto requestDto);
}
