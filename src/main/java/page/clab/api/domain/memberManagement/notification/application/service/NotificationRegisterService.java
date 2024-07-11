package page.clab.api.domain.memberManagement.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.port.in.EnsureMemberExistenceUseCase;
import page.clab.api.domain.memberManagement.notification.application.dto.request.NotificationRequestDto;
import page.clab.api.domain.memberManagement.notification.application.port.in.RegisterNotificationUseCase;
import page.clab.api.domain.memberManagement.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

@Service
@RequiredArgsConstructor
public class NotificationRegisterService implements RegisterNotificationUseCase {

    private final EnsureMemberExistenceUseCase ensureMemberExistenceUseCase;
    private final RegisterNotificationPort registerNotificationPort;

    @Transactional
    @Override
    public Long registerNotification(NotificationRequestDto requestDto) {
        ensureMemberExistenceUseCase.ensureMemberExists(requestDto.getMemberId());
        Notification notification = NotificationRequestDto.toEntity(requestDto);
        return registerNotificationPort.save(notification).getId();
    }
}
