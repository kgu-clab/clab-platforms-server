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
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.config.SuspiciousPatterns;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class InvalidEndpointAccessFilter extends GenericFilterBean {

    private final BlacklistIpRepository blacklistIpRepository;

    private final SlackService slackService;

    private final String fileURL;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        boolean isUploadedFileAccess = path.startsWith(fileURL);
        boolean isSuspicious = SuspiciousPatterns.getSuspiciousPatterns().stream().anyMatch(pattern -> pattern.matcher(path).matches());

        if (!isUploadedFileAccess && isSuspicious) {
            logAndRespondToSuspiciousAccess(httpRequest, (HttpServletResponse) response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void logAndRespondToSuspiciousAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        String httpMethod = request.getMethod();
        int httpStatus = HttpServletResponse.SC_FORBIDDEN;
        String message = "서버 내부 파일 및 디렉토리에 대한 접근이 감지되었습니다.";

        log.info("[{}:{}] {} {} {} {}", clientIpAddress, id, requestUrl, httpMethod, httpStatus, message);
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS, message);

        if (!blacklistIpRepository.existsByIpAddress(clientIpAddress)) {
            blacklistIpRepository.save(
                    BlacklistIp.builder()
                            .ipAddress(clientIpAddress)
                            .build()
            );
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.BLACKLISTED_IP_ADDED, clientIpAddress);
        }
        ResponseUtil.sendErrorResponse(response, httpStatus);
    }

}