package page.clab.api.global.common.slack.application;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.domain.SlackBoardInfo;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.common.slack.domain.AlertType;
import page.clab.api.global.common.slack.domain.GeneralAlertType;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.config.SlackConfig;
import page.clab.api.global.util.HttpReqResUtil;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SlackServiceHelper {

    private final Slack slack;

    private final String webhookUrl;

    private final String webUrl;

    private final String apiUrl;

    private final String color;

    private final Environment environment;

    private final AttributeStrategy attributeStrategy;

    public SlackServiceHelper(SlackConfig slackConfig, Environment environment, AttributeStrategy attributeStrategy) {
        this.slack = slackConfig.slack();
        this.webhookUrl = slackConfig.getWebhookUrl();
        this.webUrl = slackConfig.getWebUrl();
        this.apiUrl = slackConfig.getApiUrl();
        this.color = slackConfig.getColor();
        this.environment = environment;
        this.attributeStrategy = attributeStrategy;
    }

    public CompletableFuture<Boolean> sendSlackMessage(AlertType alertType, HttpServletRequest request, Object additionalData) {
        List<LayoutBlock> blocks = createBlocks(alertType, request, additionalData);

        return CompletableFuture.supplyAsync(() -> {
            Payload payload = Payload.builder()
                    .blocks(List.of(blocks.getFirst()))
                    .attachments(Collections.singletonList(
                            Attachment.builder()
                                    .color(color)
                                    .blocks(blocks.subList(1, blocks.size()))
                                    .build()
                    )).build();
            try {
                WebhookResponse response = slack.send(webhookUrl, payload);
                if (response.getCode() == 200) {
                    return true;
                } else {
                    log.error("Slack notification failed: {}", response.getMessage());
                    return false;
                }
            } catch (IOException e) {
                log.error("Failed to send Slack message: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public List<LayoutBlock> createBlocks(AlertType alertType, HttpServletRequest request, Object additionalData) {
        if (alertType instanceof SecurityAlertType) {
            return createSecurityAlertBlocks(request, alertType, additionalData.toString());
        } else if (alertType instanceof GeneralAlertType) {
            switch ((GeneralAlertType) alertType) {
                case ADMIN_LOGIN:
                    if (additionalData instanceof MemberLoginInfoDto) {
                        return createAdminLoginBlocks(request, (MemberLoginInfoDto) additionalData);
                    }
                    break;
                case APPLICATION_CREATED:
                    if (additionalData instanceof ApplicationRequestDto) {
                        return createApplicationBlocks((ApplicationRequestDto) additionalData);
                    }
                    break;
                case BOARD_CREATED:
                    if (additionalData instanceof SlackBoardInfo) {
                        return createBoardBlocks((SlackBoardInfo) additionalData);
                    }
                    break;
                case SERVER_START:
                    return createServerStartBlocks();
                case SERVER_ERROR:
                    if (additionalData instanceof Exception) {
                        return createErrorBlocks(request, (Exception) additionalData);
                    }
                    break;
                default:
                    log.error("Unknown alert type: {}", alertType);
                    return List.of();
            }
        }
        return List.of();
    }

    private List<LayoutBlock> createErrorBlocks(HttpServletRequest request, Exception e) {
        String httpMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String username = getUsername();

        String errorMessage = e.getMessage() == null ? "No error message provided" : e.getMessage();
        String detailedMessage = extractMessageAfterException(errorMessage);
        log.error("Server Error: {}", detailedMessage);
        return Arrays.asList(
                section(section -> section.text(markdownText(":firecracker: *Server Error*"))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + username),
                        markdownText("*Endpoint:*\n[" + httpMethod + "] " + requestUrl)
                ))),
                section(section -> section.text(markdownText("*Error Message:*\n" + detailedMessage))),
                section(section -> section.text(markdownText("*Stack Trace:*\n```" + getStackTraceSummary(e) + "```")))
        );
    }

    private List<LayoutBlock> createSecurityAlertBlocks(HttpServletRequest request, AlertType alertType, String additionalMessage) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        String username = getUsername(request);
        String location = getLocation(request);

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

    private List<LayoutBlock> createAdminLoginBlocks(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String location = getLocation(request);

        return Arrays.asList(
                section(section -> section.text(markdownText(String.format(":mechanic: *%s Login*", loginMember.getRole().getDescription())))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + loginMember.getMemberId() + " " + loginMember.getMemberName()),
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

    private List<LayoutBlock> createBoardBlocks(SlackBoardInfo board) {
        List<LayoutBlock> blocks = new ArrayList<>();

        blocks.add(section(section -> section.text(markdownText(":writing_hand: *New Board*"))));
        blocks.add(section(section -> section.fields(Arrays.asList(
                markdownText("*Title:*\n" + board.getTitle()),
                markdownText("*Category:*\n" + board.getCategory()),
                markdownText("*User:*\n" + board.getUsername())
        ))));
        return blocks;
    }

    private List<LayoutBlock> createServerStartBlocks() {
        String osInfo = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String jdkVersion = System.getProperty("java.version");

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        int availableProcessors = osBean.getAvailableProcessors();
        double systemLoadAverage = osBean.getSystemLoadAverage();
        double cpuUsage = ((systemLoadAverage / availableProcessors) * 100);

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        String memoryInfo = formatMemoryUsage(heapMemoryUsage);

        return Arrays.asList(
                section(section -> section.text(markdownText("*:battery: Server Started*"))),
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

    private String extractMessageAfterException(String message) {
        String exceptionIndicator = "Exception:";
        int exceptionIndex = message.indexOf(exceptionIndicator);
        return exceptionIndex == -1 ? message : message.substring(exceptionIndex + exceptionIndicator.length()).trim();
    }

    private String getStackTraceSummary(Exception e) {
        return Arrays.stream(e.getStackTrace())
                .limit(10)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

    private String formatMemoryUsage(MemoryUsage memoryUsage) {
        long usedMemory = memoryUsage.getUsed() / (1024 * 1024);
        long maxMemory = memoryUsage.getMax() / (1024 * 1024);
        return String.format("%dMB / %dMB (%.2f%%)", usedMemory, maxMemory, ((double) usedMemory / maxMemory) * 100);
    }

    private static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();
    }

    private @NotNull String getUsername(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(request.getAttribute("member"))
                .map(Object::toString)
                .orElseGet(() -> Optional.ofNullable(authentication)
                        .map(Authentication::getName)
                        .orElse("anonymous"));
    }

    private @NotNull String getLocation(HttpServletRequest request) {
        IPResponse ipResponse = attributeStrategy.getAttribute(request);
        return ipResponse == null ? "Unknown" : ipResponse.getCountryName() + ", " + ipResponse.getCity();
    }

}
