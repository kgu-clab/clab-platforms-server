package page.clab.api.global.common.notificationSetting.application.event;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.port.in.UpdateNotificationSettingUseCase;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;
import page.clab.api.global.common.notificationSetting.config.NotificationConfigProperties;
import page.clab.api.global.common.notificationSetting.config.NotificationConfigProperties.PlatformMapping;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

@Component
public class NotificationListener {

    private final UpdateNotificationSettingUseCase updateNotificationSettingUseCase;
    private final Map<String, NotificationSender> notificationSenders;
    private final NotificationConfigProperties notificationConfigProperties;

    public NotificationListener(
            UpdateNotificationSettingUseCase updateNotificationSettingUseCase,
            List<NotificationSender> notificationSenderList,
            NotificationConfigProperties notificationConfigProperties) {
        this.updateNotificationSettingUseCase = updateNotificationSettingUseCase;
        this.notificationConfigProperties = notificationConfigProperties;
        this.notificationSenders = notificationSenderList.stream()
                .collect(Collectors.toMap(NotificationSender::getPlatformName, Function.identity()));
    }

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        AlertType alertType = event.getAlertType();
        NotificationSetting setting = updateNotificationSettingUseCase.getOrCreateDefaultSetting(alertType);

        if (setting.isEnabled()) {
            List<NotificationConfigProperties.PlatformMapping> mappings = getMappingsForAlertType(alertType);

            if (mappings != null) {
                for (NotificationConfigProperties.PlatformMapping mapping : mappings) {
                    NotificationSender sender = notificationSenders.get(mapping.getPlatform());
                    if (sender != null) {
                        String webhookUrl = getWebhookUrl(mapping);
                        if (webhookUrl != null) {
                            sender.sendNotification(event, webhookUrl);
                        }
                    }
                }
            }
        }
    }

    private List<NotificationConfigProperties.PlatformMapping> getMappingsForAlertType(AlertType alertType) {
        String categoryName = alertType.getCategory().name();
        List<PlatformMapping> mappings =
                notificationConfigProperties.getCategoryMappings().get(categoryName);

        if (mappings == null || mappings.isEmpty()) {
            mappings = notificationConfigProperties.getDefaultMappings();
        }
        return mappings;
    }

    private String getWebhookUrl(NotificationConfigProperties.PlatformMapping mapping) {
        String platform = mapping.getPlatform();
        String webhookKey = mapping.getWebhook();
        NotificationConfigProperties.PlatformConfig platformConfig = notificationConfigProperties.getPlatforms()
                .get(platform);
        if (platformConfig != null) {
            return platformConfig.getWebhooks().get(webhookKey);
        }
        return null;
    }
}
