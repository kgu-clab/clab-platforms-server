package page.clab.api.global.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * {@code ApiLogger}는 API 요청 및 응답 로깅을 담당하는 유틸리티 클래스입니다.
 * 클라이언트 IP 주소, 사용자 정보, 요청 URL, HTTP 메서드, 상태 코드 등의 정보를 포함하여 로그를 출력합니다.
 *
 * <p>주요 기능:
 * <ul>
 *     <li>{@link #logRequest(HttpServletRequest, HttpServletResponse, String, String)} - 요청과 응답에 대한 기본 정보 로깅</li>
 *     <li>{@link #logRequestDuration(HttpServletRequest, HttpServletResponse, Exception)} - 요청의 수행 시간을 포함한 추가 정보를 로깅</li>
 * </ul>
 */
@Component
@Slf4j
public class ApiLogger {

    public void logRequest(HttpServletRequest request, HttpServletResponse response, String clientIpAddress, String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();

        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString == null ? requestUrl : requestUrl + "?" + queryString;

        String httpMethod = request.getMethod();
        int statusCode = response.getStatus();

        log.info("[{}:{}] {} {} {} {}", clientIpAddress, id, fullUrl, httpMethod, statusCode, message);
    }

    public void logRequestDuration(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();

        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString == null ? requestUrl : requestUrl + "?" + queryString;

        String httpMethod = request.getMethod();
        int statusCode = response.getStatus();

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (ex == null) {
            log.info("[{}:{}] {} {} {} {}ms", clientIpAddress, id, fullUrl, httpMethod, statusCode, duration);
        } else {
            log.error("[{}:{}] {} {} {} {}ms, Exception: {}", clientIpAddress, id, fullUrl, httpMethod, statusCode, duration, ex.getMessage());
        }
    }
}
