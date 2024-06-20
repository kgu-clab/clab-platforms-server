package page.clab.api.global.common.slack.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.global.common.slack.dao.NotificationSettingRepository;
import page.clab.api.global.common.slack.domain.AlertType;
import page.clab.api.global.common.slack.domain.NotificationSetting;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

    private final NotificationSettingRepository settingRepository;

    @Transactional
    public NotificationSetting getOrCreateDefaultSetting(AlertType alertType) {
        return settingRepository.findByAlertType(alertType)
                .orElseGet(() -> createAndSaveDefaultSetting(alertType));
    }

    private NotificationSetting createAndSaveDefaultSetting(AlertType alertType) {
        NotificationSetting defaultSetting = NotificationSetting.createDefault(alertType);
        return settingRepository.save(defaultSetting);
    }

}
