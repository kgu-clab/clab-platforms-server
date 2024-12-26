package page.clab.api.domain.memberManagement.notification.application.port.out;

import java.util.List;
import page.clab.api.domain.memberManagement.notification.domain.Notification;

public interface RegisterNotificationPort {

    Notification save(Notification notification);

    void saveAll(List<Notification> notifications);
}
