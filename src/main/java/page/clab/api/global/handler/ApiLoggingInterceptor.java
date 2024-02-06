package page.clab.api.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import page.clab.api.global.util.HttpReqResUtil;

@Component
@Slf4j
public class ApiLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (authentication == null || authentication.getName() == null) ? "anonymous" : authentication.getName();
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String requestUrl = request.getRequestURI();
        String httpMethod = request.getMethod();
        int httpStatus = response.getStatus();

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (ex == null) {
            log.info("[{}:{}] {} {} {} {}ms", clientIpAddress, id, requestUrl, httpMethod, httpStatus, duration);
        } else {
            log.error("[{}:{}] {} {} {} {}ms, Exception: {}", clientIpAddress, id, requestUrl, httpMethod, httpStatus, duration, ex.getMessage());
        }
    }

}
