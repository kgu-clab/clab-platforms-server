package page.clab.api.domain.memberManagement.notification.application.port.in;

import java.util.List;

public interface SendNotificationUseCase {
    void sendNotificationToAllMembers(String content);

    void sendNotificationToMember(String memberId, String content);

    void sendNotificationToMembers(List<String> memberIds, String content);

    void sendNotificationToAdmins(String content);

    void sendNotificationToSuperAdmins(String content);
}
