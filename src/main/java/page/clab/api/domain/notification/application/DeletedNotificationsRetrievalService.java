package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.port.in.RetrieveDeletedNotificationsUseCase;
import page.clab.api.domain.notification.application.port.out.RetrieveDeletedNotificationsPort;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedNotificationsRetrievalService implements RetrieveDeletedNotificationsUseCase {

    private final RetrieveDeletedNotificationsPort retrieveDeletedNotificationsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NotificationResponseDto> retrieve(Pageable pageable) {
        Page<Notification> notifications = retrieveDeletedNotificationsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::toDto));
    }
}
