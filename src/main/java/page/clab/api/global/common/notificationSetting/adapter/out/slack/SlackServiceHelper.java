package page.clab.api.global.common.notificationSetting.adapter.out.slack;

import static com.slack.api.model.block.Blocks.actions;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import jakarta.servlet.http.HttpServletRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.ExecutivesAlertType;
import page.clab.api.global.common.notificationSetting.domain.GeneralAlertType;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;
import page.clab.api.global.common.notificationSetting.domain.SlackBoardInfo;
import page.clab.api.global.common.notificationSetting.domain.SlackBookLoanRecordInfo;
import page.clab.api.global.common.notificationSetting.domain.SlackMembershipFeeInfo;
import page.clab.api.global.config.SlackConfig;
import page.clab.api.global.util.HttpReqResUtil;

/**
 * {@code SlackServiceHelper}는 다양한 알림 유형에 따라 Slack 메시지를 구성하고 전송하는 클래스입니다.
 *
 * <p>주요 기능:</p>
 * <ul>
 *     <li>{@link #sendSlackMessage(String, AlertType, HttpServletRequest, Object)}: Slack에 알림 메시지를 비동기적으로 전송</li>
 *     <li>{@link #createBlocks(AlertType, HttpServletRequest, Object)}: 알림 유형에 따라 Slack 메시지 블록 생성</li>
 *     <li>다양한 알림 유형에 맞는 메시지 형식을 생성하는 전용 메서드</li>
 * </ul>
 *
 * <p>Slack API와 통합하여 웹훅 URL을 통해 메시지를 전송하며, 메시지 전송 실패 시 로그에 오류를 기록합니다.</p>
 *
 * <p>AlertType을 기반으로 여러 도메인에서 발생하는 이벤트를 Slack을 통해 모니터링할 수 있도록 지원하며,
 * Slack 알림은 주로 서버 이벤트, 보안 경고, 신규 신청, 관리자 로그인 등의 이벤트를 다룹니다.</p>
 *
 * @see Slack
 * @see Payload
 * @see LayoutBlock
 */
@Component
@Slf4j
public class SlackServiceHelper {

    private final Slack slack;
    private final String webUrl;
    private final String apiUrl;
    private final String color;
    private final Environment environment;
    private final AttributeStrategy attributeStrategy;

    public SlackServiceHelper(SlackConfig slackConfig, Environment environment, AttributeStrategy attributeStrategy) {
        this.slack = slackConfig.slack();
        this.webUrl = slackConfig.getWebUrl();
        this.apiUrl = slackConfig.getApiUrl();
        this.color = slackConfig.getColor();
        this.environment = environment;
        this.attributeStrategy = attributeStrategy;
    }

    /**
     * Slack에 알림 메시지를 전송합니다.
     *
     * <p>주어진 webhookUrl과 alertType, HttpServletRequest 및 추가 데이터(additionalData)를 사용하여 알림 메시지를
     * 비동기적으로 Slack에 전송합니다.</p>
     *
     * @param webhookUrl     메시지를 보낼 Slack 웹훅 URL
     * @param alertType      알림 유형을 나타내는 {@link AlertType}
     * @param request        HttpServletRequest 객체, 클라이언트 요청 정보
     * @param additionalData 추가 데이터
     * @return 메시지 전송 성공 여부를 나타내는 CompletableFuture<Boolean>
     */
    public CompletableFuture<Boolean> sendSlackMessage(String webhookUrl, AlertType alertType,
                                                       HttpServletRequest request, Object additionalData) {
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

    /**
     * 특정 알림 유형과 요청 정보 및 추가 데이터를 사용하여 Slack 메시지의 블록을 생성합니다.
     *
     * <p>AlertType에 따라 보안 경고, 일반 알림, 운영진 알림 등 다양한 형식의 메시지를 생성합니다.</p>
     *
     * @param alertType      알림 유형
     * @param request        HttpServletRequest 객체
     * @param additionalData 추가 데이터
     * @return 생성된 LayoutBlock 목록
     */
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
        } else if (alertType instanceof ExecutivesAlertType) {
            switch ((ExecutivesAlertType) alertType) {
                case NEW_APPLICATION:
                    if (additionalData instanceof ApplicationRequestDto) {
                        return createApplicationBlocks((ApplicationRequestDto) additionalData);
                    }
                    break;
                case NEW_BOARD:
                    if (additionalData instanceof SlackBoardInfo) {
                        return createBoardBlocks((SlackBoardInfo) additionalData);
                    }
                    break;
                case NEW_MEMBERSHIP_FEE:
                    if (additionalData instanceof SlackMembershipFeeInfo) {
                        return createMembershipFeeBlocks((SlackMembershipFeeInfo) additionalData);
                    }
                    break;
                case NEW_BOOK_LOAN_REQUEST:
                    if (additionalData instanceof SlackBookLoanRecordInfo) {
                        return createBookLoanRecordBlocks((SlackBookLoanRecordInfo) additionalData);
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
        String queryString = request.getQueryString();
        String fullUrl = queryString == null ? requestUrl : requestUrl + "?" + queryString;
        String username = getUsername(request);

        String errorMessage = e.getMessage() == null ? "No error message provided" : e.getMessage();
        String detailedMessage = extractMessageAfterException(errorMessage);
        log.error("Server Error: {}", detailedMessage);
        return Arrays.asList(
                section(section -> section.text(markdownText(":firecracker: *Server Error*"))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + username),
                        markdownText("*Endpoint:*\n[" + httpMethod + "] " + fullUrl)
                ))),
                section(section -> section.text(markdownText("*Error Message:*\n" + detailedMessage))),
                section(section -> section.text(markdownText("*Stack Trace:*\n```" + getStackTraceSummary(e) + "```")))
        );
    }

    private List<LayoutBlock> createSecurityAlertBlocks(HttpServletRequest request, AlertType alertType,
                                                        String additionalMessage) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString == null ? requestUrl : requestUrl + "?" + queryString;
        String username = getUsername(request);
        String location = getLocation(request);

        return Arrays.asList(
                section(section -> section.text(markdownText(String.format(":imp: *%s*", alertType.getTitle())))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + username),
                        markdownText("*IP Address:*\n" + clientIpAddress),
                        markdownText("*Location:*\n" + location),
                        markdownText("*Endpoint:*\n" + fullUrl)
                ))),
                section(section -> section.text(
                        markdownText("*Details:*\n" + alertType.getDefaultMessage() + "\n" + additionalMessage)))
        );
    }

    private List<LayoutBlock> createAdminLoginBlocks(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String location = getLocation(request);

        return Arrays.asList(
                section(section -> section.text(
                        markdownText(String.format(":mechanic: *%s Login*", loginMember.getRole().getDescription())))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*User:*\n" + loginMember.getMemberId() + " " + loginMember.getMemberName()),
                        markdownText("*IP Address:*\n" + clientIpAddress),
                        markdownText("*Location:*\n" + location)
                )))
        );
    }

    private List<LayoutBlock> createApplicationBlocks(ApplicationRequestDto requestDto) {
        List<LayoutBlock> blocks = new ArrayList<>();

        blocks.add(section(section -> section.text(markdownText(":sparkles: *동아리 지원*"))));
        blocks.add(section(section -> section.fields(Arrays.asList(
                markdownText("*구분:*\n" + requestDto.getApplicationType().getDescription()),
                markdownText("*학번:*\n" + requestDto.getStudentId()),
                markdownText("*이름:*\n" + requestDto.getName()),
                markdownText("*학년:*\n" + requestDto.getGrade() + "학년"),
                markdownText("*관심 분야:*\n" + requestDto.getInterests())
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

        blocks.add(section(section -> section.text(markdownText(":writing_hand: *새 게시글*"))));
        blocks.add(section(section -> section.fields(Arrays.asList(
                markdownText("*제목:*\n" + board.getTitle()),
                markdownText("*분류:*\n" + board.getCategory()),
                markdownText("*작성자:*\n" + board.getUsername())
        ))));
        return blocks;
    }

    private List<LayoutBlock> createMembershipFeeBlocks(SlackMembershipFeeInfo additionalData) {
        String username = additionalData.getMemberId() + " " + additionalData.getMemberName();

        return Arrays.asList(
                section(section -> section.text(markdownText(":dollar: *회비 신청*"))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*신청자:*\n" + username),
                        markdownText("*분류:*\n" + additionalData.getCategory()),
                        markdownText("*금액:*\n" + additionalData.getAmount() + "원")
                ))),
                section(section -> section.text(markdownText("*Content:*\n" + additionalData.getContent())))
        );
    }

    private List<LayoutBlock> createBookLoanRecordBlocks(SlackBookLoanRecordInfo additionalData) {
        String username = additionalData.getMemberId() + " " + additionalData.getMemberName();

        return Arrays.asList(
                section(section -> section.text(markdownText(":books: *도서 대여 신청*"))),
                section(section -> section.fields(Arrays.asList(
                        markdownText("*도서명:*\n" + additionalData.getBookTitle()),
                        markdownText("*분류:*\n" + additionalData.getCategory()),
                        markdownText("*신청자:*\n" + username),
                        markdownText("*상태:*\n" + (additionalData.isAvailable() ? "대여 가능" : "대여 중"))
                )))
        );
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
