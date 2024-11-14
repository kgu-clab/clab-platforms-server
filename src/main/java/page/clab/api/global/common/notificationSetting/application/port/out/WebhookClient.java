package page.clab.api.global.common.notificationSetting.application.port.out;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import page.clab.api.global.common.notificationSetting.domain.AlertType;

/**
 * WebhookClient는 외부 시스템(Discord, Slack 등)과 통신하기 위한 포트 인터페이스입니다.
 */
public interface WebhookClient {

    /**
     * 특정 알림 유형과 요청 정보, 추가 데이터를 사용하여 메시지를 비동기적으로 전송합니다.
     *
     * @param webhookUrl     메시지를 보낼 Webhook URL
     * @param alertType      알림 유형
     * @param request        클라이언트 요청 정보
     * @param additionalData 추가 데이터
     * @return 메시지 전송 성공 여부를 나타내는 CompletableFuture<Boolean>
     */
    CompletableFuture<Boolean> sendMessage(String webhookUrl, AlertType alertType, HttpServletRequest request,
                                           Object additionalData);
}
