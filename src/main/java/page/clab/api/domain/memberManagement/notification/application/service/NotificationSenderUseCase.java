package page.clab.api.domain.memberManagement.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.domain.memberManagement.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.memberManagement.notification.domain.Notification;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NotificationSenderUseCase implements SendNotificationUseCase {

    private final RegisterNotificationPort registerNotificationPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Override
    @Transactional
    public void sendNotificationToAllMembers(String content) {
        List<Notification> notifications = externalRetrieveMemberUseCase.getMemberIds().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        registerNotificationPort.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotificationToMember(String memberId, String content) {
        externalRetrieveMemberUseCase.ensureMemberExists(memberId);
        Notification notification = Notification.create(memberId, content);
        registerNotificationPort.save(notification);
    }

    @Override
    @Transactional
    public void sendNotificationToMembers(List<String> memberIds, String content) {
        List<Notification> notifications = memberIds.stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        registerNotificationPort.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotificationToAdmins(String content) {
        sendNotificationToSpecificRole(externalRetrieveMemberUseCase::getAdminIds, content);
    }

    @Override
    @Transactional
    public void sendNotificationToSuperAdmins(String content) {
        sendNotificationToSpecificRole(externalRetrieveMemberUseCase::getSuperAdminIds, content);
    }

    private void sendNotificationToSpecificRole(Supplier<List<String>> memberSupplier, String content) {
        List<Notification> notifications = memberSupplier.get().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        registerNotificationPort.saveAll(notifications);
    }
}
