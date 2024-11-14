package page.clab.api.global.common.notificationSetting.adapter.out.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * {@code DiscordWebhookClient}는 다양한 알림 유형에 따라 Discord 메시지를 구성하고 전송하는 클래스입니다.
 *
 * <p>주요 기능:</p>
 * <ul>
 *     <li>{@link #sendMessage(String, AlertType, HttpServletRequest, Object)}: Discord에 알림 메시지를 비동기적으로 전송</li>
 *     <li>{@link #createEmbeds(AlertType, HttpServletRequest, Object)}: 알림 유형에 따라 Discord 메시지 임베드 생성</li>
 *     <li>다양한 알림 유형에 맞는 메시지 형식을 생성하는 전용 메서드</li>
 * </ul>
 *
 * <p>Discord Webhook API를 사용하여 웹훅 URL을 통해 메시지를 전송하며, 메시지 전송 실패 시 로그에 오류를 기록합니다.</p>
 *
 * <p>AlertType을 기반으로 여러 도메인에서 발생하는 이벤트를 Discord를 통해 모니터링할 수 있도록 지원하며,
 * Discord 알림은 주로 서버 이벤트, 보안 경고, 신규 신청, 관리자 로그인 등의 이벤트를 다룹니다.</p>
 *
 * @see HttpClient
 * @see HttpRequest
 * @see HttpResponse
 */
@Component
@Slf4j
public class DiscordWebhookClient extends AbstractWebhookClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final NotificationConfigProperties.CommonProperties commonProperties;
    private final Environment environment;
    private final WebhookCommonService webhookCommonService;

    public DiscordWebhookClient(
            NotificationConfigProperties notificationConfigProperties,
            ObjectMapper objectMapper,
            Environment environment,
            WebhookCommonService webhookCommonService
    ) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
        this.commonProperties = notificationConfigProperties.getCommon();
        this.environment = environment;
        this.webhookCommonService = webhookCommonService;
    }

    /**
     * Discord에 알림 메시지를 비동기적으로 전송합니다.
     *
     * @param webhookUrl     메시지를 보낼 Discord 웹훅 URL
     * @param alertType      알림 유형을 나타내는 {@link AlertType}
     * @param request        HttpServletRequest 객체, 클라이언트 요청 정보
     * @param additionalData 추가 데이터
     * @return 메시지 전송 성공 여부를 나타내는 CompletableFuture<Boolean>
     */
    public CompletableFuture<Boolean> sendMessage(String webhookUrl, AlertType alertType,
                                                  HttpServletRequest request, Object additionalData) {
        Map<String, Object> payload = createPayload(alertType, request, additionalData);

        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonPayload = objectMapper.writeValueAsString(payload);

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(webhookUrl))
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == HttpStatus.NO_CONTENT.value()) {
                    return true;
                } else {
                    log.error("Discord notification failed: {}", response.body());
                    return false;
                }
            } catch (IOException | InterruptedException e) {
                log.error("Failed to send Discord message: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    /**
     * 알림 유형과 요청 정보, 추가 데이터를 사용하여 Discord 메시지 페이로드를 생성합니다.
     *
     * @param alertType      알림 유형
     * @param request        클라이언트 요청 정보
     * @param additionalData 추가 데이터
     * @return 생성된 페이로드 맵
     */
    public Map<String, Object> createPayload(AlertType alertType, HttpServletRequest request, Object additionalData) {
        List<Map<String, Object>> embeds = createEmbeds(alertType, request, additionalData);

        Map<String, Object> payload = new HashMap<>();
        payload.put("embeds", embeds);

        return payload;
    }

    /**
     * 특정 알림 유형과 요청 정보 및 추가 데이터를 사용하여 Discord 메시지의 임베드를 생성합니다.
     *
     * @param alertType      알림 유형
     * @param request        HttpServletRequest 객체
     * @param additionalData 추가 데이터
     * @return 생성된 임베드 목록
     */
    public List<Map<String, Object>> createEmbeds(AlertType alertType, HttpServletRequest request,
                                                  Object additionalData) {
        switch (alertType) {
            case SecurityAlertType securityAlertType -> {
                return createSecurityAlertEmbeds(request, alertType, additionalData.toString());
            }
            case GeneralAlertType generalAlertType -> {
                return createGeneralAlertEmbeds(generalAlertType, request, additionalData);
            }
            case ExecutivesAlertType executivesAlertType -> {
                return createExecutivesAlertEmbeds(executivesAlertType, additionalData);
            }
            case null, default -> {
                log.error("Unknown alert type: {}", alertType);
                return Collections.emptyList();
            }
        }
    }

    private List<Map<String, Object>> createGeneralAlertEmbeds(GeneralAlertType alertType, HttpServletRequest request,
                                                               Object additionalData) {
        switch (alertType) {
            case ADMIN_LOGIN:
                if (additionalData instanceof MemberLoginInfoDto) {
                    return createAdminLoginEmbeds(request, (MemberLoginInfoDto) additionalData);
                }
                break;
            case SERVER_START:
                return createServerStartEmbeds();
            case SERVER_ERROR:
                if (additionalData instanceof Exception) {
                    return createErrorEmbeds(request, (Exception) additionalData);
                }
                break;
            default:
                log.error("Unknown general alert type: {}", alertType);
        }
        return Collections.emptyList();
    }

    private List<Map<String, Object>> createExecutivesAlertEmbeds(ExecutivesAlertType alertType,
                                                                  Object additionalData) {
        switch (alertType) {
            case NEW_APPLICATION:
                if (additionalData instanceof ApplicationRequestDto) {
                    return createApplicationEmbeds((ApplicationRequestDto) additionalData);
                }
                break;
            case NEW_BOARD:
                if (additionalData instanceof BoardNotificationInfo) {
                    return createBoardEmbeds((BoardNotificationInfo) additionalData);
                }
                break;
            case NEW_MEMBERSHIP_FEE:
                if (additionalData instanceof MembershipFeeNotificationInfo) {
                    return createMembershipFeeEmbeds((MembershipFeeNotificationInfo) additionalData);
                }
                break;
            case NEW_BOOK_LOAN_REQUEST:
                if (additionalData instanceof BookLoanRecordNotificationInfo) {
                    return createBookLoanRecordEmbeds((BookLoanRecordNotificationInfo) additionalData);
                }
                break;
            default:
                log.error("Unknown executives alert type: {}", alertType);
        }
        return Collections.emptyList();
    }

    private List<Map<String, Object>> createErrorEmbeds(HttpServletRequest request, Exception e) {
        String httpMethod = request.getMethod();
        String fullUrl = webhookCommonService.getFullUrl(request);
        String username = webhookCommonService.getUsername(request);
        String errorMessage = webhookCommonService.extractMessageAfterException(e);
        String stackTrace = webhookCommonService.getStackTraceSummary(e);

        log.error("Server Error: {}", errorMessage);

        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":firecracker: Server Error");
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("User", username, true),
                createField("Endpoint", "[" + httpMethod + "] " + fullUrl, true),
                createField("Error Message", errorMessage, false),
                createField("Stack Trace", "```" + stackTrace + "```", false)
        ));

        return Collections.singletonList(embed);
    }

    private List<Map<String, Object>> createSecurityAlertEmbeds(HttpServletRequest request, AlertType alertType,
                                                                String additionalMessage) {
        String clientIp = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String fullUrl = webhookCommonService.getFullUrl(request);
        String username = webhookCommonService.getUsername(request);
        String location = webhookCommonService.getLocation(request);

        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":imp: " + alertType.getTitle());
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("User", username, true),
                createField("IP Address", clientIp, true),
                createField("Location", location, true),
                createField("Endpoint", fullUrl, true),
                createField("Details", alertType.getDefaultMessage() + "\n" + additionalMessage, false)
        ));

        return Collections.singletonList(embed);
    }

    private List<Map<String, Object>> createAdminLoginEmbeds(HttpServletRequest request,
                                                             MemberLoginInfoDto loginMember) {
        String clientIp = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String location = webhookCommonService.getLocation(request);

        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":mechanic: " + loginMember.getRole().getDescription() + " Login");
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("User", loginMember.getMemberId() + " " + loginMember.getMemberName(), true),
                createField("IP Address", clientIp, true),
                createField("Location", location, true)
        ));

        return Collections.singletonList(embed);
    }

    private List<Map<String, Object>> createApplicationEmbeds(ApplicationRequestDto requestDto) {
        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":sparkles: 동아리 지원");
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("구분", requestDto.getApplicationType().getDescription(), true),
                createField("학번", requestDto.getStudentId(), true),
                createField("이름", requestDto.getName(), true),
                createField("학년", requestDto.getGrade() + "학년", true),
                createField("관심 분야", requestDto.getInterests(), false)
        ));

        if (requestDto.getGithubUrl() != null && !requestDto.getGithubUrl().isEmpty()) {
            embed.put("description", "[Github](" + requestDto.getGithubUrl() + ")");
        }

        return Collections.singletonList(embed);
    }

    private List<Map<String, Object>> createBoardEmbeds(BoardNotificationInfo board) {
        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":writing_hand: 새 게시글");
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("제목", board.getTitle(), true),
                createField("분류", board.getCategory(), true),
                createField("작성자", board.getUsername(), true)
        ));

        return Collections.singletonList(embed);
    }

    private List<Map<String, Object>> createMembershipFeeEmbeds(MembershipFeeNotificationInfo data) {
        String username = data.getMemberId() + " " + data.getMemberName();

        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":dollar: 회비 신청");
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("신청자", username, true),
                createField("분류", data.getCategory(), true),
                createField("금액", data.getAmount() + "원", true),
                createField("Content", data.getContent(), false)
        ));

        return Collections.singletonList(embed);
    }

    private List<Map<String, Object>> createBookLoanRecordEmbeds(BookLoanRecordNotificationInfo data) {
        String username = data.getMemberId() + " " + data.getMemberName();

        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":books: 도서 대여 신청");
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("도서명", data.getBookTitle(), true),
                createField("분류", data.getCategory(), true),
                createField("신청자", username, true),
                createField("상태", data.isAvailable() ? "대여 가능" : "대여 중", true)
        ));

        return Collections.singletonList(embed);
    }

    private List<Map<String, Object>> createServerStartEmbeds() {
        String osInfo = webhookCommonService.getOperatingSystemInfo();
        String jdkVersion = webhookCommonService.getJavaRuntimeVersion();
        double cpuUsage = webhookCommonService.getCpuUsage();
        String memoryUsage = webhookCommonService.getMemoryUsage();

        Map<String, Object> embed = new HashMap<>();
        embed.put("title", ":battery: Server Started");
        embed.put("color", commonProperties.getColorAsInt());
        embed.put("fields", Arrays.asList(
                createField("Environment", environment.getProperty("spring.profiles.active"), true),
                createField("OS", osInfo, true),
                createField("JDK Version", jdkVersion, true),
                createField("CPU Usage", String.format("%.2f%%", cpuUsage), true),
                createField("Memory Usage", memoryUsage, true)
        ));

        embed.put("description",
                "[Web](" + commonProperties.getWebUrl() + ") | [API Docs](" + commonProperties.getApiUrl() + ")");

        return Collections.singletonList(embed);
    }

    private Map<String, Object> createField(String name, String value, boolean inline) {
        Map<String, Object> field = new HashMap<>();
        field.put("name", name);
        field.put("value", value);
        field.put("inline", inline);
        return field;
    }
}
