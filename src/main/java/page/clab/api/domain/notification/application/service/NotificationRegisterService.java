package page.clab.api.domain.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.EnsureMemberExistenceUseCase;
import page.clab.api.domain.notification.application.port.in.RegisterNotificationUseCase;
import page.clab.api.domain.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class NotificationRegisterService implements RegisterNotificationUseCase {

    private final EnsureMemberExistenceUseCase ensureMemberExistenceUseCase;
    private final ValidationService validationService;
    private final RegisterNotificationPort registerNotificationPort;

    @Transactional
    @Override
    public Long registerNotification(NotificationRequestDto requestDto) {
        ensureMemberExistenceUseCase.ensureMemberExists(requestDto.getMemberId());
        Notification notification = NotificationRequestDto.toEntity(requestDto);
        validationService.checkValid(notification);
        return registerNotificationPort.save(notification).getId();
    }
}
