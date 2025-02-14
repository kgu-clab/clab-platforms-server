package page.clab.api.global.common.notificationSetting.application.service;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * {@code WebhookCommonService}는 Webhook 클라이언트에서 공통으로 사용되는 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class WebhookCommonService {

    private final AttributeStrategy attributeStrategy;

    public String getFullUrl(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        return queryString == null ? requestUrl : requestUrl + "?" + queryString;
    }

    public String extractMessageAfterException(Exception e) {
        String errorMessage = Optional.ofNullable(e.getMessage()).orElse("No error message provided");
        String exceptionIndicator = "Exception:";
        int index = errorMessage.indexOf(exceptionIndicator);
        return index == -1 ? errorMessage : errorMessage.substring(index + exceptionIndicator.length()).trim();
    }

    public String getStackTraceSummary(Exception e) {
        return Arrays.stream(e.getStackTrace())
            .limit(10)
            .map(StackTraceElement::toString)
            .collect(Collectors.joining("\n"));
    }

    public String getOperatingSystemInfo() {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        return osName + " " + osVersion;
    }

    public String getJavaRuntimeVersion() {
        return System.getProperty("java.version");
    }

    public double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        int processors = osBean.getAvailableProcessors();
        double systemLoadAverage = osBean.getSystemLoadAverage();
        return (systemLoadAverage / processors) * 100;
    }

    public String getMemoryUsage() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();

        long used = memoryUsage.getUsed() / (1024 * 1024);
        long max = memoryUsage.getMax() / (1024 * 1024);

        return String.format("%dMB / %dMB (%.2f%%)", used, max, ((double) used / max) * 100);
    }

    public String getUsername(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(request.getAttribute("member"))
            .map(Object::toString)
            .orElseGet(() -> Optional.ofNullable(auth)
                .map(Authentication::getName)
                .orElse("anonymous"));
    }

    public String getLocation(HttpServletRequest request) {
        IPResponse ipResponse = attributeStrategy.getAttribute(request);
        return ipResponse == null ? "Unknown" : ipResponse.getCountryName() + ", " + ipResponse.getCity();
    }
}
