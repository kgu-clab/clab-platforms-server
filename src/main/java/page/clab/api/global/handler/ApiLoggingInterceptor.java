package page.clab.api.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import page.clab.api.global.util.ApiLogger;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private final ApiLogger apiLogger;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler)
        throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @NotNull Object handler,
        Exception ex) throws Exception {
        apiLogger.logRequestDuration(request, response, ex);
    }
}
