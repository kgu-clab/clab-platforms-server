package page.clab.api.global.common.notificationSetting.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findByAlertType(AlertType alertType);
}
