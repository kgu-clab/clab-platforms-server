package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRegisterBlacklistIpUseCase;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRetrieveBlacklistIpUseCase;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.SecurityPatternChecker;

/**
 * {@code InvalidEndpointAccessFilter}는 서버 내부 파일 및 디렉토리에 대한 비정상적인 접근을 차단하고 보안 경고를 전송하는 필터입니다.
 *
 * <p>특정 패턴을 통해 비정상적인 접근 시도를 탐지하며, 비정상적인 경로로 접근을 시도한 IP를
 * 블랙리스트에 등록하고, Slack을 통해 보안 경고 메시지를 전송합니다.</p>
 *
 * <p>이 필터는 다음과 같은 검증을 수행합니다:</p>
 * <ul>
 *     <li>파일 접근 URL(fileURL)로 시작하는 요청을 제외하고, 보안 패턴에 맞는 경로에 대한 비정상 접근 여부 확인</li>
 *     <li>비정상적인 접근이 감지될 경우 해당 IP를 블랙리스트에 추가</li>
 *     <li>블랙리스트에 추가된 IP에 대해 Slack 알림을 전송하여 보안팀에 경고</li>
 * </ul>
 *
 * <p>비정상적인 접근 시도에 대해서는 403 Forbidden 응답을 반환합니다.</p>
 *
 * @see GenericFilterBean
 */
@RequiredArgsConstructor
@Slf4j
public class InvalidEndpointAccessFilter extends GenericFilterBean {

    private final String fileURL;
    private final ExternalRegisterBlacklistIpUseCase externalRegisterBlacklistIpUseCase;
    private final ExternalRetrieveBlacklistIpUseCase externalRetrieveBlacklistIpUseCase;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        boolean isUploadedFileAccess = path.startsWith(fileURL);
        boolean isSuspicious = SecurityPatternChecker.matchesSuspiciousPattern(path);

        if (!isUploadedFileAccess && isSuspicious) {
            handleSuspiciousAccess(httpRequest, (HttpServletResponse) response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void handleSuspiciousAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        int statusCode = HttpServletResponse.SC_FORBIDDEN;

        logSuspiciousAccess(request, clientIpAddress);
        sendSecurityAlertIfNotBlacklisted(request, clientIpAddress);

        ResponseUtil.sendErrorResponse(response, statusCode);
    }

    private void logSuspiciousAccess(HttpServletRequest request, String clientIpAddress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id =
                (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();
        String requestUrl = request.getRequestURI();
        String httpMethod = request.getMethod();
        int statusCode = HttpServletResponse.SC_FORBIDDEN;
        String message = "서버 내부 파일 및 디렉토리에 대한 접근이 감지되었습니다.";

        log.info("[{}:{}] {} {} {} {}", clientIpAddress, id, requestUrl, httpMethod, statusCode, message);
    }

    private void sendSecurityAlertIfNotBlacklisted(HttpServletRequest request, String clientIpAddress) {
        if (!externalRetrieveBlacklistIpUseCase.existsByIpAddress(clientIpAddress)) {
            externalRegisterBlacklistIpUseCase.save(
                    BlacklistIp.create(clientIpAddress, "서버 내부 파일 및 디렉토리에 대한 접근 시도")
            );
            sendSecurityAlerts(request, clientIpAddress);
        }
    }

    private void sendSecurityAlerts(HttpServletRequest request, String clientIpAddress) {
        String abnormalAccessMessage = "서버 내부 파일 및 디렉토리에 대한 접근이 감지되었습니다.";
        String blacklistAddedMessage = "Added IP: " + clientIpAddress;

        eventPublisher.publishEvent(new NotificationEvent(this, SecurityAlertType.ABNORMAL_ACCESS, request,
                abnormalAccessMessage));
        eventPublisher.publishEvent(new NotificationEvent(this, SecurityAlertType.BLACKLISTED_IP_ADDED, request,
                blacklistAddedMessage));
    }
}
