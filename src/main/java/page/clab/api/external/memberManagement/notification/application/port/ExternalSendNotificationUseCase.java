package page.clab.api.external.memberManagement.notification.application.port;

import java.util.List;

public interface ExternalSendNotificationUseCase {

    void sendNotificationToAllMembers(String content);

    void sendNotificationToMember(String memberId, String content);

    void sendNotificationToMembers(List<String> memberIds, String content);

    void sendNotificationToAdmins(String content);

    void sendNotificationToSuperAdmins(String content);
}
