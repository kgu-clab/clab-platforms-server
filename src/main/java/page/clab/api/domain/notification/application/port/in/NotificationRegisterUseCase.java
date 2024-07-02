package page.clab.api.domain.notification.application.port.in;

import page.clab.api.domain.notification.dto.request.NotificationRequestDto;

public interface NotificationRegisterUseCase {
    Long register(NotificationRequestDto requestDto);
}
