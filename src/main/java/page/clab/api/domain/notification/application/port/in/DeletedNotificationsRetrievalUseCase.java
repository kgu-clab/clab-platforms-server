package page.clab.api.domain.notification.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedNotificationsRetrievalUseCase {
    PagedResponseDto<NotificationResponseDto> retrieve(Pageable pageable);
}
