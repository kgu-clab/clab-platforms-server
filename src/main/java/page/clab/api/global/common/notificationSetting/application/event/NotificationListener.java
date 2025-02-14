package page.clab.api.global.common.notificationSetting.application.event;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.port.in.ManageNotificationSettingUseCase;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;
import page.clab.api.global.common.notificationSetting.config.NotificationConfigProperties;
import page.clab.api.global.common.notificationSetting.config.NotificationConfigProperties.PlatformConfig;
import page.clab.api.global.common.notificationSetting.config.NotificationConfigProperties.PlatformMapping;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

@Component
@Slf4j
public class NotificationListener {

    private final ManageNotificationSettingUseCase manageNotificationSettingUseCase;
    private final Map<String, NotificationSender> notificationSenders;
    private final NotificationConfigProperties notificationConfigProperties;

    public NotificationListener(
        ManageNotificationSettingUseCase manageNotificationSettingUseCase,
        List<NotificationSender> notificationSenderList,
        NotificationConfigProperties notificationConfigProperties) {
        this.manageNotificationSettingUseCase = manageNotificationSettingUseCase;
        this.notificationConfigProperties = notificationConfigProperties;
        this.notificationSenders = notificationSenderList.stream()
            .collect(Collectors.toMap(NotificationSender::getPlatformName, Function.identity()));
    }

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        AlertType alertType = event.getAlertType();

        NotificationSetting setting = manageNotificationSettingUseCase.getOrCreateDefaultSetting(alertType);
        if (!setting.isEnabled()) {
            return;
        }

        List<PlatformMapping> mappings = getMappingsForAlertType(alertType);
        if (mappings.isEmpty()) {
            return;
        }

        mappings.forEach(mapping -> getWebhookUrl(mapping)
            .ifPresent(webhookUrl -> sendNotification(mapping.getPlatform(), event, webhookUrl)));
    }

    private List<PlatformMapping> getMappingsForAlertType(AlertType alertType) {
        String categoryName = alertType.getCategory().name();
        Map<String, List<PlatformMapping>> categoryMappings = notificationConfigProperties.getCategoryMappings();

        return Optional.ofNullable(categoryMappings.get(categoryName))
            .filter(list -> !list.isEmpty())
            .orElseGet(notificationConfigProperties::getDefaultMappings);
    }

    private Optional<String> getWebhookUrl(PlatformMapping mapping) {
        String platform = mapping.getPlatform();
        String webhookKey = mapping.getWebhook();
        Map<String, PlatformConfig> platforms = notificationConfigProperties.getPlatforms();

        return Optional.ofNullable(platforms.get(platform))
            .map(platformConfig -> platformConfig.getWebhooks().get(webhookKey))
            .map(url -> {
                log.debug("Found webhook URL for platform '{}', key '{}': {}", platform, webhookKey, url);
                return url;
            })
            .or(() -> {
                log.warn("No webhook URL found for platform '{}', key '{}'", platform, webhookKey);
                return Optional.empty();
            });
    }

    private void sendNotification(String platform, NotificationEvent event, String webhookUrl) {
        NotificationSender sender = notificationSenders.get(platform);
        if (sender == null) {
            log.warn("No NotificationSender found for platform: {}", platform);
            return;
        }

        try {
            sender.sendNotification(event, webhookUrl);
            log.debug("Notification sent via platform: {}", platform);
        } catch (Exception e) {
            log.error("Failed to send notification via platform: {}", platform, e);
        }
    }
}
