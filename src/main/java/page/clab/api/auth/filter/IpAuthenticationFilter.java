package page.clab.api.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import page.clab.api.util.GeoIpUtil;
import page.clab.api.util.HttpReqResUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.net.InetAddress;

@Component
@Slf4j
public class IpAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String country = GeoIpUtil.getInfoByIp(inetAddress.toString()).getCountry();
        if(country != null && !country.equals("South Korea")){
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