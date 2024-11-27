package page.clab.api.global.common.notificationSetting.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.global.common.notificationSetting.application.dto.mapper.NotificationSettingDtoMapper;
import page.clab.api.global.common.notificationSetting.application.dto.response.NotificationSettingResponseDto;
import page.clab.api.global.common.notificationSetting.application.port.in.RetrieveNotificationSettingUseCase;
import page.clab.api.global.common.notificationSetting.application.port.out.RetrieveNotificationSettingPort;

/**
 * {@code RetrieveNotificationSettingService}는 알림 설정을 조회하는 서비스입니다.
 *
 * <p>이 서비스는 알림 설정의 전체 목록을 조회할 수 있는 기능을 제공합니다.</p>
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>{@link #retrieveNotificationSettings()} - 모든 알림 설정을 조회합니다.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class RetrieveNotificationSettingService implements RetrieveNotificationSettingUseCase {

    private final RetrieveNotificationSettingPort retrieveNotificationSettingPort;
    private final NotificationSettingDtoMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<NotificationSettingResponseDto> retrieveNotificationSettings() {
        return retrieveNotificationSettingPort.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }
}
