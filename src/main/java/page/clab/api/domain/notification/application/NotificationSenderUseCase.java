package page.clab.api.domain.notification.application;

import java.util.List;

public interface NotificationSenderUseCase {
    void sendNotificationToAllMembers(String content);
    void sendNotificationToMember(String memberId, String content);
    void sendNotificationToMembers(List<String> memberIds, String content);
    void sendNotificationToAdmins(String content);
    void sendNotificationToSuperAdmins(String content);
}

