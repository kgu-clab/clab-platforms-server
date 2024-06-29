package page.clab.api.domain.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.DeletedNotificationsRetrievalService;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedNotificationsRetrievalServiceImpl implements DeletedNotificationsRetrievalService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NotificationResponseDto> retrieveDeleted(Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::toDto));
    }
}
