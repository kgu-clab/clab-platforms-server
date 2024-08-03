package page.clab.api.domain.memberManagement.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.notification.application.dto.response.NotificationResponseDto;
import page.clab.api.domain.memberManagement.notification.application.port.in.RetrieveDeletedNotificationsUseCase;
import page.clab.api.domain.memberManagement.notification.application.port.out.RetrieveNotificationPort;
import page.clab.api.domain.memberManagement.notification.domain.Notification;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedNotificationsRetrievalService implements RetrieveDeletedNotificationsUseCase {

    private final RetrieveNotificationPort retrieveNotificationPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NotificationResponseDto> retrieveDeletedNotifications(Pageable pageable) {
        Page<Notification> notifications = retrieveNotificationPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::toDto));
    }
}
