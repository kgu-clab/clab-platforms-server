package page.clab.api.global.auth.filter;

import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.IPInfoUtil;

import java.io.IOException;

@Component
@Slf4j
public class IpAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        IPResponse ipResponse = IPInfoUtil.getIpInfo((HttpServletRequest) request);
        String country = ipResponse == null ? null : ipResponse.getCountryCode();
        if (country != null && !country.equals("KR")) {
            log.info("[{}:{}] 허용되지 않은 국가로부터의 접근입니다.", ipAddress, country);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("IP Authentication Filter Init..");
    }

    @Override
    public void destroy() {
        log.info("IP Authentication Filter Destroy..");
    }

}