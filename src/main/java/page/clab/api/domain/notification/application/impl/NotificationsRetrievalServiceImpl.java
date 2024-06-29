package page.clab.api.domain.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.notification.application.NotificationsRetrievalService;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class NotificationsRetrievalServiceImpl implements NotificationsRetrievalService {

    private final MemberLookupService memberLookupService;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NotificationResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Notification> notifications = notificationRepository.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::toDto));
    }
}
