package page.clab.api.global.filter;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import page.clab.api.global.config.IPInfoConfig;

/**
 * {@code IPinfoSpringFilter}는 IP 정보 조회 및 관리 기능을 제공하는 필터로, 클라이언트의 IP 주소를 기반으로 IP 정보 조회 API를 통해 세부 정보를 가져와 요청에 저장합니다.
 *
 * <p>이 필터는 요청이 IP 정보가 필요한 경우에만 IP 정보를 조회하며,
 * 요청 객체에 IP 정보를 속성으로 저장하여 이후 처리 로직에서 사용할 수 있도록 합니다.</p>
 */
@Slf4j
public class IPinfoSpringFilter implements Filter {

    private final IPinfo ii;
    private final AttributeStrategy attributeStrategy;
    private final IPStrategy ipStrategy;
    private final InterceptorStrategy interceptorStrategy;

    public IPinfoSpringFilter(IPInfoConfig ipInfoConfig) {
        this.ii = ipInfoConfig.ipInfo();
        this.attributeStrategy = ipInfoConfig.attributeStrategy();
        this.ipStrategy = ipInfoConfig.ipStrategy();
        this.interceptorStrategy = ipInfoConfig.interceptorStrategy();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("Initializing IPinfoSpringFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIpAddress = ipStrategy.getIPAddress(httpRequest);

        if (shouldProcessRequest(httpRequest, clientIpAddress)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            IPResponse ipResponse = ii.lookupIP(clientIpAddress);
            attributeStrategy.storeAttribute(httpRequest, ipResponse);
        } catch (Exception e) {
            log.error("Error while fetching IP information: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private boolean shouldProcessRequest(HttpServletRequest httpRequest, String clientIpAddress) {
        return !interceptorStrategy.shouldRun(httpRequest)
            || attributeStrategy.hasAttribute(httpRequest)
            || Objects.isNull(clientIpAddress);
    }

    @Override
    public void destroy() {
        log.info("Destroying IPinfoSpringFilter");
    }
}
