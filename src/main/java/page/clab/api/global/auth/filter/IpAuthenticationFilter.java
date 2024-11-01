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

/**
 * IpAuthenticationFilter는 IP 주소를 기반으로 클라이언트의 접근 권한을 검증하는 필터입니다.
 *
 * <p>클라이언트의 IP 주소를 사용하여 국가 정보를 조회하고, 허용된 국가 목록에 포함되지 않은 경우
 * 접근을 차단합니다. 주로 국가별 접근 제한이 필요한 상황에서 사용됩니다.</p>
 *
 * <p>이 필터는 IP 정보를 조회하기 위해 외부 IP 조회 서비스(IPinfo)를 사용하며,
 * 국가 정보를 확인하여 허용된 국가 목록과 비교합니다. 허용되지 않은 국가에서 접근 시
 * 로그 경고 메시지를 기록하고 요청 체인을 종료합니다.</p>
 *
 * <p>필터는 다음과 같은 주요 기능을 수행합니다:</p>
 * <ul>
 *     <li>클라이언트의 IP 주소를 기반으로 국가 정보를 조회</li>
 *     <li>허용된 국가 목록에 포함되지 않은 경우 접근 차단</li>
 * </ul>
 *
 * <p>필터 초기화와 종료 시 로그 메시지를 기록하여 필터의 활성 상태를 확인할 수 있습니다.</p>
 *
 * @see Filter
 */
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
