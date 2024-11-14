package page.clab.api.global.common.notificationSetting.application.port.out;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import page.clab.api.global.common.notificationSetting.domain.AlertType;

public interface WebhookClient {

    CompletableFuture<Boolean> sendMessage(String webhookUrl, AlertType alertType, HttpServletRequest request,
                                           Object additionalData);
}
