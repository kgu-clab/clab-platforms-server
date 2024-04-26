package page.clab.api.global.auth.filter;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class IpAuthenticationFilter implements Filter {

    private final AttributeStrategy attributeStrategy;

    public IpAuthenticationFilter(AttributeStrategy attributeStrategy) {
        this.attributeStrategy = attributeStrategy;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        IPResponse ipResponse = attributeStrategy.getAttribute((HttpServletRequest) request);
        if (ipResponse == null) {
            log.warn("No IP information found in the request.");
            chain.doFilter(request, response);
            return;
        }

        String country = ipResponse.getCountryCode();
        if (country != null && !country.equals("KR")) {
            log.warn("Access from non-permitted country: {}", country);
            return;
        }

        chain.doFilter(request, response);
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