package page.clab.api.global.common.notificationSetting.adapter.out.webhook;

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
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberLoginInfoDto;
import page.clab.api.global.common.notificationSetting.adapter.out.webhook.common.AbstractWebhookClient;
import page.clab.api.global.common.notificationSetting.application.dto.notification.BoardNotificationInfo;
import page.clab.api.global.common.notificationSetting.application.dto.notification.BookLoanRecordNotificationInfo;
import page.clab.api.global.common.notificationSetting.application.dto.notification.MembershipFeeNotificationInfo;
import page.clab.api.global.common.notificationSetting.application.service.WebhookCommonService;
import page.clab.api.global.common.notificationSetting.config.NotificationConfigProperties;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.ExecutivesAlertType;
import page.clab.api.global.common.notificationSetting.domain.GeneralAlertType;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;

/**
 * {@code SlackWebhookClient}는 다양한 알림 유형에 따라 Slack 메시지를 구성하고 전송하는 클래스입니다.
 *
 * <p>주요 기능:</p>
 * <ul>
 *     <li>{@link #sendMessage(String, AlertType, HttpServletRequest, Object)}: Slack에 알림 메시지를 비동기적으로 전송</li>
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
public class SlackWebhookClient extends AbstractWebhookClient {

    private final Slack slack;
    private final NotificationConfigProperties.CommonProperties commonProperties;
    private final Environment environment;
    private final WebhookCommonService webhookCommonService;

    public SlackWebhookClient(
            NotificationConfigProperties notificationConfigProperties,
            Environment environment,
            WebhookCommonService webhookCommonService
    ) {
        this.slack = Slack.getInstance();
        this.commonProperties = notificationConfigProperties.getCommon();
        this.environment = environment;
        this.webhookCommonService = webhookCommonService;
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
    public CompletableFuture<Boolean> sendMessage(String webhookUrl, AlertType alertType,
                                                  HttpServletRequest request, Object additionalData) {
        List<LayoutBlock> blocks = createBlocks(alertType, request, additionalData);

        return CompletableFuture.supplyAsync(() -> {
            Payload payload = Payload.builder()
                    .blocks(Collections.singletonList(blocks.getFirst()))
                    .attachments(Collections.singletonList(
                            Attachment.builder()
                                    .color(commonProperties.getColor())
                                    .blocks(blocks.subList(1, blocks.size()))
                                    .build()
                    ))
                    .build();

            try {
                WebhookResponse response = slack.send(webhookUrl, payload);
                if (response.getCode() == HttpStatus.OK.value()) {
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
        switch (alertType) {
            case SecurityAlertType securityAlertType -> {
                return createSecurityAlertBlocks(request, alertType, additionalData.toString());
            }
            case GeneralAlertType generalAlertType -> {
                return createGeneralAlertBlocks(generalAlertType, request, additionalData);
            }
            case ExecutivesAlertType executivesAlertType -> {
                return createExecutivesAlertBlocks(executivesAlertType, additionalData);
            }
            case null, default -> {
                log.error("Unknown alert type: {}", alertType);
                return Collections.emptyList();
            }
        }
    }

    private List<LayoutBlock> createGeneralAlertBlocks(GeneralAlertType alertType, HttpServletRequest request,
                                                       Object additionalData) {
        switch (alertType) {
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
                log.error("Unknown general alert type: {}", alertType);
        }
        return Collections.emptyList();
    }

    private List<LayoutBlock> createExecutivesAlertBlocks(ExecutivesAlertType alertType, Object additionalData) {
        switch (alertType) {
            case NEW_APPLICATION:
                if (additionalData instanceof ApplicationRequestDto) {
                    return createApplicationBlocks((ApplicationRequestDto) additionalData);
                }
                break;
            case NEW_BOARD:
                if (additionalData instanceof BoardNotificationInfo) {
                    return createBoardBlocks((BoardNotificationInfo) additionalData);
                }
                break;
            case NEW_MEMBERSHIP_FEE:
                if (additionalData instanceof MembershipFeeNotificationInfo) {
                    return createMembershipFeeBlocks((MembershipFeeNotificationInfo) additionalData);
                }
                break;
            case NEW_BOOK_LOAN_REQUEST:
                if (additionalData instanceof BookLoanRecordNotificationInfo) {
                    return createBookLoanRecordBlocks((BookLoanRecordNotificationInfo) additionalData);
                }
                break;
            default:
                log.error("Unknown executives alert type: {}", alertType);
        }
        return Collections.emptyList();
    }

    private List<LayoutBlock> createErrorBlocks(HttpServletRequest request, Exception e) {
        String httpMethod = request.getMethod();
        String fullUrl = webhookCommonService.getFullUrl(request);
        String username = webhookCommonService.getUsername(request);
        String errorMessage = webhookCommonService.extractMessageAfterException(e);
        String stackTrace = webhookCommonService.getStackTraceSummary(e);

        log.error("Server Error: {}", errorMessage);

        return Arrays.asList(
                section(s -> s.text(markdownText(":firecracker: *Server Error*"))),
                section(s -> s.fields(Arrays.asList(
                        markdownText("*User:*\n" + username),
                        markdownText("*Endpoint:*\n[" + httpMethod + "] " + fullUrl)
                ))),
                section(s -> s.text(markdownText("*Error Message:*\n" + errorMessage))),
                section(s -> s.text(markdownText("*Stack Trace:*\n```" + stackTrace + "```")))
        );
    }

    private List<LayoutBlock> createSecurityAlertBlocks(HttpServletRequest request, AlertType alertType,
                                                        String additionalMessage) {
        String clientIp = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String fullUrl = webhookCommonService.getFullUrl(request);
        String username = webhookCommonService.getUsername(request);
        String location = webhookCommonService.getLocation(request);

        return Arrays.asList(
                section(s -> s.text(markdownText(":imp: *" + alertType.getTitle() + "*"))),
                section(s -> s.fields(Arrays.asList(
                        markdownText("*User:*\n" + username),
                        markdownText("*IP Address:*\n" + clientIp),
                        markdownText("*Location:*\n" + location),
                        markdownText("*Endpoint:*\n" + fullUrl)
                ))),
                section(s -> s.text(
                        markdownText("*Details:*\n" + alertType.getDefaultMessage() + "\n" + additionalMessage)))
        );
    }

    private List<LayoutBlock> createAdminLoginBlocks(HttpServletRequest request, MemberLoginInfoDto loginMember) {
        String clientIp = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String location = webhookCommonService.getLocation(request);

        return Arrays.asList(
                section(s -> s.text(markdownText(":mechanic: *" + loginMember.getRole().getDescription() + " Login*"))),
                section(s -> s.fields(Arrays.asList(
                        markdownText("*User:*\n" + loginMember.getMemberId() + " " + loginMember.getMemberName()),
                        markdownText("*IP Address:*\n" + clientIp),
                        markdownText("*Location:*\n" + location)
                )))
        );
    }

    private List<LayoutBlock> createApplicationBlocks(ApplicationRequestDto requestDto) {
        List<LayoutBlock> blocks = new ArrayList<>();

        blocks.add(section(s -> s.text(markdownText(":sparkles: *동아리 지원*"))));
        blocks.add(section(s -> s.fields(Arrays.asList(
                markdownText("*구분:*\n" + requestDto.getApplicationType().getDescription()),
                markdownText("*학번:*\n" + requestDto.getStudentId()),
                markdownText("*이름:*\n" + requestDto.getName()),
                markdownText("*학년:*\n" + requestDto.getGrade() + "학년"),
                markdownText("*관심 분야:*\n" + requestDto.getInterests())
        ))));

        if (requestDto.getGithubUrl() != null && !requestDto.getGithubUrl().isEmpty()) {
            blocks.add(actions(a -> a.elements(asElements(
                    button(b -> b.text(plainText(pt -> pt.emoji(true).text("Github")))
                            .url(requestDto.getGithubUrl())
                            .actionId("click_github"))
            ))));
        }

        return blocks;
    }

    private List<LayoutBlock> createBoardBlocks(BoardNotificationInfo board) {
        return Arrays.asList(
                section(s -> s.text(markdownText(":writing_hand: *새 게시글*"))),
                section(s -> s.fields(Arrays.asList(
                        markdownText("*제목:*\n" + board.getTitle()),
                        markdownText("*분류:*\n" + board.getCategory()),
                        markdownText("*작성자:*\n" + board.getUsername())
                )))
        );
    }

    private List<LayoutBlock> createMembershipFeeBlocks(MembershipFeeNotificationInfo data) {
        String username = data.getMemberId() + " " + data.getMemberName();

        return Arrays.asList(
                section(s -> s.text(markdownText(":dollar: *회비 신청*"))),
                section(s -> s.fields(Arrays.asList(
                        markdownText("*신청자:*\n" + username),
                        markdownText("*분류:*\n" + data.getCategory()),
                        markdownText("*금액:*\n" + data.getAmount() + "원")
                ))),
                section(s -> s.text(markdownText("*Content:*\n" + data.getContent())))
        );
    }

    private List<LayoutBlock> createBookLoanRecordBlocks(BookLoanRecordNotificationInfo data) {
        String username = data.getMemberId() + " " + data.getMemberName();

        return Arrays.asList(
                section(s -> s.text(markdownText(":books: *도서 대여 신청*"))),
                section(s -> s.fields(Arrays.asList(
                        markdownText("*도서명:*\n" + data.getBookTitle()),
                        markdownText("*분류:*\n" + data.getCategory()),
                        markdownText("*신청자:*\n" + username),
                        markdownText("*상태:*\n" + (data.isAvailable() ? "대여 가능" : "대여 중"))
                )))
        );
    }

    private List<LayoutBlock> createServerStartBlocks() {
        String osInfo = webhookCommonService.getOperatingSystemInfo();
        String jdkVersion = webhookCommonService.getJavaRuntimeVersion();
        double cpuUsage = webhookCommonService.getCpuUsage();
        String memoryUsage = webhookCommonService.getMemoryUsage();

        return Arrays.asList(
                section(s -> s.text(markdownText(":battery: *Server Started*"))),
                section(s -> s.fields(Arrays.asList(
                        markdownText("*Environment:* \n" + environment.getProperty("spring.profiles.active")),
                        markdownText("*OS:* \n" + osInfo),
                        markdownText("*JDK Version:* \n" + jdkVersion),
                        markdownText("*CPU Usage:* \n" + String.format("%.2f%%", cpuUsage)),
                        markdownText("*Memory Usage:* \n" + memoryUsage)
                ))),
                actions(a -> a.elements(asElements(
                        button(b -> b.text(plainText(pt -> pt.emoji(true).text("Web")))
                                .url(commonProperties.getWebUrl())
                                .value("click_web")),
                        button(b -> b.text(plainText(pt -> pt.emoji(true).text("API Docs")))
                                .url(commonProperties.getApiUrl())
                                .value("click_apiDocs"))
                )))
        );
    }
}
