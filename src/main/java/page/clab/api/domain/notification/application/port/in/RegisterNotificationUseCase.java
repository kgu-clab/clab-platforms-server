package page.clab.api.domain.notification.application.port.in;

import page.clab.api.domain.notification.application.dto.request.NotificationRequestDto;

public interface RegisterNotificationUseCase {
    Long registerNotification(NotificationRequestDto requestDto);
}