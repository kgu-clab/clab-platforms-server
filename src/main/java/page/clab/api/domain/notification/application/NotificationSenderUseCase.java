package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberExistenceUseCase;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.notification.domain.Notification;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NotificationSenderUseCase implements page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase {

    private final MemberExistenceUseCase memberExistenceUseCase;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final RegisterNotificationPort registerNotificationPort;

    @Override
    @Transactional
    public void sendNotificationToAllMembers(String content) {
        List<Notification> notifications = memberInfoRetrievalUseCase.getMemberIds().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        registerNotificationPort.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotificationToMember(String memberId, String content) {
        memberExistenceUseCase.ensureMemberExists(memberId);
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
        sendNotificationToSpecificRole(memberInfoRetrievalUseCase::getAdminIds, content);
    }

    @Override
    @Transactional
    public void sendNotificationToSuperAdmins(String content) {
        sendNotificationToSpecificRole(memberInfoRetrievalUseCase::getSuperAdminIds, content);
    }

    private void sendNotificationToSpecificRole(Supplier<List<String>> memberSupplier, String content) {
        List<Notification> notifications = memberSupplier.get().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        registerNotificationPort.saveAll(notifications);
    }
}
