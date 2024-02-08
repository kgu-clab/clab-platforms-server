package page.clab.api.global.common.slack.application;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class SlackService {

    private final Slack slack = Slack.getInstance();
    private final String webhookUrl;

    public SlackService(@Value("${slack.webhook.url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public boolean sendServerErrorNotification(HttpServletRequest request, Exception e) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        String errorLocation = e.getStackTrace()[0].toString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        String message = String.format("[Server Error Alert]\n- Time: %s\n- username: %s\n- IP: %s\n- Endpoint: %s\n- Location: %s\n- Error: %s",
                serverTime, username, clientIpAddress, requestUrl, errorLocation, e.getMessage());
        return sendSlackMessage(message);
    }

    public boolean sendSecurityAlertNotification(HttpServletRequest request, SecurityAlertType alertType) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        String message = String.format("[Security Alert - %s]\n- Time: %s\n- username: %s\n- IP: %s\n- Endpoint: %s\n- Details: %s",
                alertType.getTitle(), serverTime, username, clientIpAddress, requestUrl, alertType.getDefaultMessage());
        return sendSlackMessage(message);
    }

    public boolean sendSecurityAlertNotification(HttpServletRequest request, SecurityAlertType alertType, String additionalMessage) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        String message = String.format("[Security Alert - %s]\n- Time: %s\n- username: %s\n- IP: %s\n- Endpoint: %s\n- Details: %s\n%s",
                alertType.getTitle(), serverTime, username, clientIpAddress, requestUrl, alertType.getDefaultMessage(), additionalMessage);
        return sendSlackMessage(message);
    }

    public boolean sendAdminLoginNotification(String username, Role role) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();

        String message = String.format("[%s Login Alert]\n- Time: %s\n- username: %s\n- IP: %s",
                role.getDescription(), serverTime, username, clientIpAddress);
        return sendSlackMessage(message);
    }

    private boolean sendSlackMessage(String message) {
        Payload payload = Payload.builder().text(message).build();
        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
            return response.getCode() == 200;
        } catch (IOException e) {
            log.error("Error sending slack message: {}", e.getMessage(), e);
            return false;
        }
    }

}
