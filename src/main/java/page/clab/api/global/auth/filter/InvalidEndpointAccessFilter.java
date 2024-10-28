package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRegisterBlacklistIpUseCase;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRetrieveBlacklistIpUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.SecurityPatternChecker;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class InvalidEndpointAccessFilter extends GenericFilterBean {

    private final SlackService slackService;
    private final String fileURL;
    private final ExternalRegisterBlacklistIpUseCase externalRegisterBlacklistIpUseCase;
    private final ExternalRetrieveBlacklistIpUseCase externalRetrieveBlacklistIpUseCase;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
        String id = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();
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

        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS, abnormalAccessMessage);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, blacklistAddedMessage);
    }
}
