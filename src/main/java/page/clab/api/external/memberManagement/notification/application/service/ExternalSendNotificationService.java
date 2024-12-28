package page.clab.api.external.memberManagement.notification.application.service;

import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.memberManagement.notification.domain.Notification;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;

@Service
@RequiredArgsConstructor
public class ExternalSendNotificationService implements ExternalSendNotificationUseCase {

    private final RegisterNotificationPort registerNotificationPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;

    @Transactional
    @Override
    public void sendNotificationToAllMembers(String content) {
        List<Notification> notifications = externalRetrieveMemberUseCase.getMemberIds().stream()
            .map(memberId -> Notification.create(memberId, content))
            .toList();
        registerNotificationPort.saveAll(notifications);
    }

    @Transactional
    @Override
    public void sendNotificationToMember(String memberId, String content) {
        externalRetrieveMemberUseCase.ensureMemberExists(memberId);
        Notification notification = Notification.create(memberId, content);
        registerNotificationPort.save(notification);
    }

    @Transactional
    @Override
    public void sendNotificationToMembers(List<String> memberIds, String content) {
        List<Notification> notifications = memberIds.stream()
            .map(memberId -> Notification.create(memberId, content))
            .toList();
        registerNotificationPort.saveAll(notifications);
    }

    @Transactional
    @Override
    public void sendNotificationToAdmins(String content) {
        sendNotificationToSpecificRole(externalRetrieveMemberUseCase::getAdminIds, content);
    }

    @Transactional
    @Override
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
