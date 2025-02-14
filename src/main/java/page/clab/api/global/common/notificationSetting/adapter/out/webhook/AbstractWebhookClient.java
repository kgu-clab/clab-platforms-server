package page.clab.api.global.common.notificationSetting.adapter.out.webhook;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import page.clab.api.global.common.notificationSetting.application.port.out.WebhookClient;
import page.clab.api.global.common.notificationSetting.domain.AlertType;

/**
 * {@code AbstractWebhookClient}는 Discord 및 Slack Webhook 클라이언트의 공통 인터페이스를 정의하는 추상 클래스입니다.
 */
public abstract class AbstractWebhookClient implements WebhookClient {

    @Override
    public abstract CompletableFuture<Boolean> sendMessage(String webhookUrl, AlertType alertType,
        HttpServletRequest request,
        Object additionalData);
}
