package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.EnsureMemberExistenceUseCase;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.domain.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.notification.domain.Notification;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NotificationSenderUseCase implements SendNotificationUseCase {

    private final EnsureMemberExistenceUseCase ensureMemberExistenceUseCase;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RegisterNotificationPort registerNotificationPort;

    @Override
    @Transactional
    public void sendNotificationToAllMembers(String content) {
        List<Notification> notifications = retrieveMemberInfoUseCase.getMemberIds().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        registerNotificationPort.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotificationToMember(String memberId, String content) {
        ensureMemberExistenceUseCase.ensureMemberExists(memberId);
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
        sendNotificationToSpecificRole(retrieveMemberInfoUseCase::getAdminIds, content);
    }

    @Override
    @Transactional
    public void sendNotificationToSuperAdmins(String content) {
        sendNotificationToSpecificRole(retrieveMemberInfoUseCase::getSuperAdminIds, content);
    }

    private void sendNotificationToSpecificRole(Supplier<List<String>> memberSupplier, String content) {
        List<Notification> notifications = memberSupplier.get().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        registerNotificationPort.saveAll(notifications);
    }
}
