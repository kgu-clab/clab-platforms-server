package page.clab.api.global.common.slack.application;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class SlackService {

    private final Slack slack = Slack.getInstance();
    private final String webhookUrl;

    public SlackService(@Value("${slack.webhook.url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public CompletableFuture<Boolean> sendServerErrorNotification(HttpServletRequest request, Exception e) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        String errorLocation = e.getStackTrace()[0].toString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        String message = String.format(":red_circle: *Server Error [%s]- %s*\n>*User*: %s\n>*Endpoint*: %s\n>*Error*: `%s`\n>```%s```",
                clientIpAddress, serverTime, username, requestUrl, errorLocation, e.getMessage());
        return sendSlackMessage(message);
    }

    public CompletableFuture<Boolean> sendSecurityAlertNotification(HttpServletRequest request, SecurityAlertType alertType, String additionalMessage) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        String message = String.format(":red_circle: *%s [%s] - %s*\n>*User*: %s\n>*Endpoint*: %s\n>*Details*: `%s`\n>```%s```",
                alertType.getTitle(), clientIpAddress, serverTime, username, requestUrl, alertType.getDefaultMessage(), additionalMessage);
        return sendSlackMessage(message);
    }

    public CompletableFuture<Boolean> sendAdminLoginNotification(String username, Role role) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();

        String message = String.format(":large_yellow_circle: *%s Login [%s] - %s*\n>*User*: %s",
                role.getDescription(), clientIpAddress, serverTime, username);
        return sendSlackMessage(message);
    }

    public CompletableFuture<Boolean> sendApplicationNotification(HttpServletRequest request, ApplicationRequestDto applicationRequestDto) {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String message = String.format(":sparkles: *New Application [%s] - %s*\n>*Type*: %s\n>*Student*: %s %s\n>*Grade*: %s\n>*Interests*: %s\n>*Github*: %s",
                clientIpAddress, serverTime, applicationRequestDto.getApplicationType().getDescription(), applicationRequestDto.getStudentId(), applicationRequestDto.getName(), applicationRequestDto.getGrade(), applicationRequestDto.getInterests(), applicationRequestDto.getGithubUrl());
        return sendSlackMessage(message);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void sendServerStartNotification() {
        String serverTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String osInfo = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String jdkVersion = System.getProperty("java.version");

        OperatingSystemMXBean osbean = ManagementFactory.getOperatingSystemMXBean();
        int availableProcessors = osbean.getAvailableProcessors();
        double systemLoadAverage = osbean.getSystemLoadAverage();
        double cpuUsage = ((systemLoadAverage / availableProcessors) * 100);

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long usedMemory = heapMemoryUsage.getUsed() / (1024 * 1024);
        long maxMemory = heapMemoryUsage.getMax() / (1024 * 1024);
        double usagePercentage = ((double) usedMemory / maxMemory) * 100;

        String message = String.format(":rocket: *Server Started*\n>*Server Time*: %s\n>*OS*: %s\n>*JDK Version*: %s\n>*CPU Usage*: %.2f%%\n>*Memory Usage*: %dMB / %dMB (%.2f%%)",
                serverTime, osInfo, jdkVersion, cpuUsage, usedMemory, maxMemory, usagePercentage);
        sendSlackMessage(message);
    }

    private CompletableFuture<Boolean> sendSlackMessage(String message) {
        Payload payload = Payload.builder().text(message).build();
        return CompletableFuture.supplyAsync(() -> {
            try {
                WebhookResponse response = slack.send(webhookUrl, payload);
                return response.getCode() == 200;
            } catch (IOException e) {
                log.error("Error sending slack message: {}", e.getMessage(), e);
                return false;
            }
        });
    }

}
