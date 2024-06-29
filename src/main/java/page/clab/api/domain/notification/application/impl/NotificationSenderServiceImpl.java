package page.clab.api.domain.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.notification.application.NotificationSenderService;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.domain.Notification;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NotificationSenderServiceImpl implements NotificationSenderService {

    private final MemberLookupService memberLookupService;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void sendNotificationToAllMembers(String content) {
        List<Notification> notifications = memberLookupService.findAllMembers().stream()
                .map(member -> Notification.create(member.getId(), content))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotificationToMember(String memberId, String content) {
        memberLookupService.ensureMemberExists(memberId);
        Notification notification = Notification.create(memberId, content);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void sendNotificationToMembers(List<String> memberIds, String content) {
        List<Notification> notifications = memberIds.stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotificationToAdmins(String content) {
        sendNotificationToSpecificRole(memberLookupService::getAdminIds, content);
    }

    @Override
    @Transactional
    public void sendNotificationToSuperAdmins(String content) {
        sendNotificationToSpecificRole(memberLookupService::getSuperAdminIds, content);
    }

    private void sendNotificationToSpecificRole(Supplier<List<String>> memberSupplier, String content) {
        List<Notification> notifications = memberSupplier.get().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        notificationRepository.saveAll(notifications);
    }
}

