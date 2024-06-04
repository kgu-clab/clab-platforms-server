package page.clab.api.global.auth.filter;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import page.clab.api.global.config.IPInfoConfig;
import page.clab.api.global.util.HttpReqResUtil;

import java.io.IOException;

@Component
@Slf4j
public class IpAuthenticationFilter implements Filter {

    private final IPinfo ipInfo;

    private final AttributeStrategy attributeStrategy;

    private final InterceptorStrategy interceptorStrategy;

    public IpAuthenticationFilter(IPInfoConfig ipInfoConfig) {
        ipInfo = ipInfoConfig.ipInfo();
        attributeStrategy = ipInfoConfig.attributeStrategy();
        interceptorStrategy = ipInfoConfig.interceptorStrategy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        try {
            if (shouldProcessRequest(httpRequest, clientIpAddress)) {
                chain.doFilter(request, response);
                return;
            }
            IPResponse ipResponse = storeIpInformation(clientIpAddress, httpRequest);
            if (isNonPermittedCountry(ipResponse)) {
                log.warn("[{}:{}] Access from non-permitted country", clientIpAddress, ipResponse.getCountryName());
                return;
            }
        } catch (RateLimitedException e) {
            log.error("Rate limit exceeded while getting IP information.");
        } catch (Exception e) {
            log.error("Failed to get IP information.");
        }
        chain.doFilter(request, response);
    }

    private boolean shouldProcessRequest(HttpServletRequest httpRequest, String clientIpAddress) {
        return !interceptorStrategy.shouldRun(httpRequest)
                || attributeStrategy.hasAttribute(httpRequest)
                || HttpReqResUtil.isBogonRequest(clientIpAddress);
    }

    private IPResponse storeIpInformation(String clientIpAddress, HttpServletRequest httpRequest) throws RateLimitedException {
        IPResponse ipResponse = ipInfo.lookupIP(clientIpAddress);
        attributeStrategy.storeAttribute(httpRequest, ipResponse);
        return ipResponse;
    }

    private boolean isNonPermittedCountry(IPResponse ipResponse) {
        String country = ipResponse.getCountryCode();
        return country != null && !country.equals("KR");
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("IP Authentication Filter initialized.");
    }

    @Override
    public void destroy() {
        log.info("IP Authentication Filter destroyed.");
    }

}