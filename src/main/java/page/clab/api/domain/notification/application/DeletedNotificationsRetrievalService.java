package page.clab.api.domain.notification.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedNotificationsRetrievalService {
    PagedResponseDto<NotificationResponseDto> retrieveDeleted(Pageable pageable);
}
