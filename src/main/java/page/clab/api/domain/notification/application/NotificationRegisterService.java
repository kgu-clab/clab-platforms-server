package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberExistenceUseCase;
import page.clab.api.domain.notification.application.port.in.NotificationRegisterUseCase;
import page.clab.api.domain.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class NotificationRegisterService implements NotificationRegisterUseCase {

    private final MemberExistenceUseCase memberExistenceUseCase;
    private final ValidationService validationService;
    private final RegisterNotificationPort registerNotificationPort;

    @Transactional
    @Override
    public Long register(NotificationRequestDto requestDto) {
        memberExistenceUseCase.ensureMemberExists(requestDto.getMemberId());
        Notification notification = NotificationRequestDto.toEntity(requestDto);
        validationService.checkValid(notification);
        return registerNotificationPort.save(notification).getId();
    }
}
