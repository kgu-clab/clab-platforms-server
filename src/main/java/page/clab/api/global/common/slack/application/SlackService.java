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
import page.clab.api.domain.member.domain.Member;
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
import java.util.ArrayList;
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

    public void sendServerErrorNotification(HttpServletRequest request, Exception e) {
        List<LayoutBlock> blocks = createErrorBlocks(request, e);
        sendSlackMessageWithBlocks(blocks);
    }

    public void sendSecurityAlertNotification(HttpServletRequest request, SecurityAlertType alertType, String additionalMessage) {
        List<LayoutBlock> blocks = createSecurityAlertBlocks(request, alertType, additionalMessage);
        sendSlackMessageWithBlocks(blocks);
    }

    public void sendAdminLoginNotification(HttpServletRequest request, Member loginMember) {
        List<LayoutBlock> blocks = createAdminLoginBlocks(request, loginMember);
        sendSlackMessageWithBlocks(blocks);
    }

    public void sendApplicationNotification(ApplicationRequestDto applicationRequestDto) {
        List<LayoutBlock> blocks = createApplicationBlocks(applicationRequestDto);
        sendSlackMessageWithBlocks(blocks);
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

    private List<LayoutBlock> createErrorBlocks(HttpServletRequest request, Exception e) {
        String httpMethod = request.getMethod();
        String requestUrl = request.getRequestURI();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        return Arrays.asList(
                section(section -> section.text(markdownText(":firecracker: *Server Error*"))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + username),
                        markdownText("*Endpoint:*\n[" + httpMethod + "] " + requestUrl)
                ))),
                section(section -> section.text(markdownText("*Error Message:*\n" + e.getMessage().split(":")[1]))),
                section(section -> section.text(markdownText("*Stack Trace:*\n```" + Arrays.toString(e.getStackTrace()) + "```")))
        );
    }

    private List<LayoutBlock> createSecurityAlertBlocks(HttpServletRequest request, SecurityAlertType alertType, String additionalMessage) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        IPResponse ipResponse = attributeStrategy.getAttribute(request);
        String location = ipResponse == null ? "Unknown" : ipResponse.getCountryName() + ", " + ipResponse.getCity();

        return Arrays.asList(
                section(section -> section.text(markdownText(String.format(":imp: *%s*", alertType.getTitle())))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + username),
                        markdownText("*IP Address:*\n" + clientIpAddress),
                        markdownText("*Location:*\n" + location),
                        markdownText("*Endpoint:*\n" + requestUrl)
                ))),
                section(section -> section.text(markdownText("*Details:*\n" + alertType.getDefaultMessage() + "\n" + additionalMessage)))
        );
    }

    private List<LayoutBlock> createAdminLoginBlocks(HttpServletRequest request, Member loginMember) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        IPResponse ipResponse = attributeStrategy.getAttribute(request);
        String location = ipResponse == null ? "Unknown" : ipResponse.getCountryName() + ", " + ipResponse.getCity();

        return Arrays.asList(
                section(section -> section.text(markdownText(String.format(":mechanic: *%s Login*", loginMember.getRole().getDescription())))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + loginMember.getId() + " " + loginMember.getName()),
                        markdownText("*IP Address:*\n" + clientIpAddress),
                        markdownText("*Location:*\n" + location)
                )))
        );
    }

    private List<LayoutBlock> createApplicationBlocks(ApplicationRequestDto requestDto) {
        List<LayoutBlock> blocks = new ArrayList<>();

        blocks.add(section(section -> section.text(markdownText(":sparkles: *New Application*"))));
        blocks.add(section(section -> section.fields(Arrays.asList(
                markdownText("*Type:*\n" + requestDto.getApplicationType().getDescription()),
                markdownText("*Student ID:*\n" + requestDto.getStudentId()),
                markdownText("*Name:*\n" + requestDto.getName()),
                markdownText("*Grade:*\n" + requestDto.getGrade() + "학년"),
                markdownText("*Interests:*\n" + requestDto.getInterests())
        ))));

        if (requestDto.getGithubUrl() != null && !requestDto.getGithubUrl().isEmpty()) {
            blocks.add(actions(actions -> actions.elements(asElements(
                    button(b -> b.text(plainText(pt -> pt.emoji(true).text("Github")))
                            .url(requestDto.getGithubUrl())
                            .actionId("click_github"))
            ))));
        }
        return blocks;
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
