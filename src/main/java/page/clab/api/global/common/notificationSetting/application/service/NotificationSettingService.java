package page.clab.api.global.common.notificationSetting.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.global.common.notificationSetting.adapter.out.persistence.NotificationSettingRepository;
import page.clab.api.global.common.notificationSetting.application.dto.mapper.NotificationSettingDtoMapper;
import page.clab.api.global.common.notificationSetting.application.dto.response.NotificationSettingResponseDto;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.AlertTypeResolver;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

/**
 * {@code NotificationSettingService}는 알림 설정을 조회 및 업데이트하는 서비스입니다.
 *
 * <p>이 서비스는 알림 유형에 따라 활성화 또는 비활성화할 수 있는 설정 기능을 제공하며,
 * 기본 알림 설정을 생성하거나 조회할 수 있습니다.</p>
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>{@link #getNotificationSettings()} - 모든 알림 설정을 조회합니다.</li>
 *     <li>{@link #updateNotificationSetting(String, boolean)} - 주어진 알림 유형에 대해 알림 설정을 업데이트합니다.</li>
 *     <li>{@link #getOrCreateDefaultSetting(AlertType)} - 주어진 알림 유형에 대한 기본 알림 설정을 조회하거나, 존재하지 않으면 생성합니다.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class NotificationSettingService {

    private final AlertTypeResolver alertTypeResolver;
    private final NotificationSettingRepository settingRepository;
    private final NotificationSettingDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<NotificationSettingResponseDto> getNotificationSettings() {
        return settingRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public void updateNotificationSetting(String alertTypeName, boolean enabled) {
        AlertType alertType = alertTypeResolver.resolve(alertTypeName);
        NotificationSetting setting = getOrCreateDefaultSetting(alertType);
        setting.updateEnabled(enabled);
        settingRepository.save(setting);
    }

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
