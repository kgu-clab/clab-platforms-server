package page.clab.api.global.common.notificationSetting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.global.common.notificationSetting.application.port.in.ToggleNotificationSettingUseCase;
import page.clab.api.global.common.notificationSetting.application.port.out.RetrieveNotificationSettingPort;
import page.clab.api.global.common.notificationSetting.application.port.out.UpdateNotificationSettingPort;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.AlertTypeResolver;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

/**
 * {@code UpdateNotificationSettingService}는 알림 설정을 업데이트하는 서비스입니다.
 *
 * <p>이 서비스는 주어진 알림 유형에 따라 활성화 또는 비활성화할 수 있는 설정을 업데이트할 수 있습니다.
 * 또한, 기본 알림 설정이 존재하지 않으면 생성하여 제공합니다.</p>
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>{@link #toggleNotificationSetting(String, boolean)} - 주어진 알림 유형에 대해 알림 설정을 업데이트합니다.</li>
 *     <li>{@link #getOrCreateDefaultSetting(AlertType)} - 주어진 알림 유형에 대한 기본 알림 설정을 조회하거나, 존재하지 않으면 생성합니다.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class ToggleNotificationSettingService implements ToggleNotificationSettingUseCase {

    private final AlertTypeResolver alertTypeResolver;
    private final RetrieveNotificationSettingPort retrieveNotificationSettingPort;
    private final UpdateNotificationSettingPort updateNotificationSettingPort;

    @Transactional
    @Override
    public void toggleNotificationSetting(String alertTypeName, boolean enabled) {
        AlertType alertType = alertTypeResolver.resolve(alertTypeName);
        NotificationSetting setting = getOrCreateDefaultSetting(alertType);
        setting.updateEnabled(enabled);
        updateNotificationSettingPort.save(setting);
    }

    @Transactional
    public NotificationSetting getOrCreateDefaultSetting(AlertType alertType) {
        return retrieveNotificationSettingPort.findByAlertType(alertType)
                .orElseGet(() -> createAndSaveDefaultSetting(alertType));
    }

    private NotificationSetting createAndSaveDefaultSetting(AlertType alertType) {
        NotificationSetting defaultSetting = NotificationSetting.createDefault(alertType);
        return updateNotificationSettingPort.save(defaultSetting);
    }
}
