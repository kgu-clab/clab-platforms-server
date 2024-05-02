package page.clab.api.global.common.slack.application;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Attachments;
import static com.slack.api.model.block.Blocks.actions;
import static com.slack.api.model.block.Blocks.section;
import com.slack.api.model.block.LayoutBlock;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.config.SlackConfig;
import page.clab.api.global.util.HttpReqResUtil;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class SlackService {

    private final Slack slack;

    private final String webhookUrl;

    private final String webUrl;

    private final String apiUrl;

    private final String color;

    private final Environment environment;

    private final AttributeStrategy attributeStrategy;

    public SlackService(SlackConfig slackConfig, Environment environment, AttributeStrategy attributeStrategy) {
        this.slack = slackConfig.slack();
        this.webhookUrl = slackConfig.getWebhookUrl();
        this.webUrl = slackConfig.getWebUrl();
        this.apiUrl = slackConfig.getApiUrl();
        this.color = slackConfig.getColor();
        this.environment = environment;
        this.attributeStrategy = attributeStrategy;
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
        IPResponse ipResponse = attributeStrategy.getAttribute(request);
        String location = ipResponse == null ? "Unknown" : ipResponse.getCountryName() + ", " + ipResponse.getCity();

        String message = String.format(":red_circle: *%s [%s] - %s*\n>*User*: %s\n>*Location*: %s\n>*Endpoint*: %s\n>*Details*: `%s`\n>```%s```",
                alertType.getTitle(), clientIpAddress, serverTime, username, location, requestUrl, alertType.getDefaultMessage(), additionalMessage);
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
        String osInfo = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String jdkVersion = System.getProperty("java.version");

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        int availableProcessors = osBean.getAvailableProcessors();
        double systemLoadAverage = osBean.getSystemLoadAverage();
        double cpuUsage = ((systemLoadAverage / availableProcessors) * 100);

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        String memoryInfo = formatMemoryUsage(heapMemoryUsage);

        List<LayoutBlock> blocks = createServerStartBlocks(osInfo, jdkVersion, cpuUsage, memoryInfo);
        sendSlackMessageWithBlocks(blocks);
    }

    private List<LayoutBlock> createServerStartBlocks(String osInfo, String jdkVersion, double cpuUsage, String memoryInfo) {
        return Arrays.asList(
                section(section -> section.text(markdownText("*:rocket: Server Started*"))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*Environment:* \n" + environment.getProperty("spring.profiles.active")),
                        markdownText("*OS:* \n" + osInfo),
                        markdownText("*JDK Version:* \n" + jdkVersion),
                        markdownText("*CPU Usage:* \n" + String.format("%.2f%%", cpuUsage)),
                        markdownText("*Memory Usage:* \n" + memoryInfo)
                ))),
                actions(actions -> actions.elements(asElements(
                        button(b -> b.text(plainText(pt -> pt.emoji(true).text("Web")))
                                .url(webUrl)
                                .value("click_web")),
                        button(b -> b.text(plainText(pt -> pt.emoji(true).text("Swagger")))
                                .url(apiUrl)
                                .value("click_swagger"))
                )))
        );
    }

    private String formatMemoryUsage(MemoryUsage memoryUsage) {
        long usedMemory = memoryUsage.getUsed() / (1024 * 1024);
        long maxMemory = memoryUsage.getMax() / (1024 * 1024);
        return String.format("%dMB / %dMB (%.2f%%)", usedMemory, maxMemory, ((double) usedMemory / maxMemory) * 100);
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

    private void sendSlackMessageWithBlocks(List<LayoutBlock> blocks) {
        Payload payload = Payload.builder().
                attachments(
                        Attachments.asAttachments(
                            Attachment.builder()
                                .color(color)
                                .blocks(blocks)
                                .build()
                        )
                ).build();
        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
            if (response.getCode() != 200) {
                log.error("Slack notification failed: {}", response.getMessage());
            }
        } catch (IOException e) {
            log.error("Failed to send Slack message: {}", e.getMessage(), e);
        }
    }

}
