package page.clab.api.global.auth.filter;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import page.clab.api.global.config.IPInfoConfig;
import page.clab.api.global.util.HttpReqResUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class IpAuthenticationFilter implements Filter {

    private final IPinfo ipInfo;

    @Value("${security.access.allowed-countries}")
    private List<String> allowedCountries;

    public IpAuthenticationFilter(IPInfoConfig ipInfoConfig) {
        ipInfo = ipInfoConfig.ipInfo();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("IP Authentication Filter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        try {
            IPResponse ipResponse = ipInfo.lookupIP(clientIpAddress);
            if (isNonPermittedCountry(ipResponse)) {
                log.warn("[{}:{}] Access from non-permitted country", clientIpAddress, ipResponse.getCountryName());
                return;
            }
        } catch (Exception e) {
            log.error("Failed to get IP information.");
        }
        chain.doFilter(request, response);
    }

    private boolean isNonPermittedCountry(IPResponse ipResponse) {
        String country = ipResponse.getCountryCode();
        return Objects.nonNull(country) && !allowedCountries.contains(country);
    }

    @Override
    public void destroy() {
        log.info("IP Authentication Filter destroyed.");
    }

}