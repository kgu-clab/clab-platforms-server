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
import lombok.extern.slf4j.Slf4j;
import page.clab.api.global.config.IPInfoConfig;

import java.io.IOException;
import java.util.Objects;

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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
