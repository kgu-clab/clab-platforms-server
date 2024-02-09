package page.clab.api.global.auth.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import page.clab.api.global.util.GeoIpUtil;
import page.clab.api.global.util.HttpReqResUtil;

@Component
@Slf4j
public class IpAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String country = GeoIpUtil.getInfoByIp(inetAddress.toString()).getCountry();
        if (country != null && !country.equals("South Korea")) {
            log.info("Access Rejected : {}, {}", ipAddress, country);
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