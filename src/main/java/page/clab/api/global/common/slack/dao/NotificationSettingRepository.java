package page.clab.api.global.common.slack.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.global.common.slack.domain.AlertType;
import page.clab.api.global.common.slack.domain.NotificationSetting;

import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findByAlertType(AlertType alertType);
}
