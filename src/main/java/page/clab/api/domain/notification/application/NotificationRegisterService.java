package page.clab.api.domain.notification.application;

import page.clab.api.domain.notification.dto.request.NotificationRequestDto;

public interface NotificationRegisterService {
    Long register(NotificationRequestDto requestDto);
}
