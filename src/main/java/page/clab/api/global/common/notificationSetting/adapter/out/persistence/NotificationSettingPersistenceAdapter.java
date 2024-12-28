package page.clab.api.global.common.notificationSetting.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.port.out.RetrieveNotificationSettingPort;
import page.clab.api.global.common.notificationSetting.application.port.out.UpdateNotificationSettingPort;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

@Component
@RequiredArgsConstructor
public class NotificationSettingPersistenceAdapter implements
    RetrieveNotificationSettingPort,
    UpdateNotificationSettingPort {

    private final NotificationSettingRepository repository;

    @Override
    public List<NotificationSetting> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<NotificationSetting> findByAlertType(AlertType alertType) {
        return repository.findByAlertType(alertType);
    }

    @Override
    public NotificationSetting save(NotificationSetting setting) {
        return repository.save(setting);
    }
}
