package page.clab.api.global.util;

import io.ipinfo.api.request.IPRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * {@code HttpReqResUtil}은 HTTP 요청 및 응답에 관련된 유틸리티 메서드를 제공하는 클래스입니다. 이 클래스는 클라이언트 IP 주소를 가져오거나, IP 주소가 비공개 IP 대역인지 확인하는
 * 등의 기능을 포함합니다.
 */
public class HttpReqResUtil {

    private static final IPRequest.IpAddressMatcher[] IpAddressMatcherList = {
        // IPv4
        new IPRequest.IpAddressMatcher("0.0.0.0/8"),
        new IPRequest.IpAddressMatcher("10.0.0.0/8"),
        new IPRequest.IpAddressMatcher("100.64.0.0/10"),
        new IPRequest.IpAddressMatcher("127.0.0.0/8"),
        new IPRequest.IpAddressMatcher("169.254.0.0/16"),
        new IPRequest.IpAddressMatcher("172.16.0.0/12"),
        new IPRequest.IpAddressMatcher("192.0.0.0/24"),
        new IPRequest.IpAddressMatcher("192.0.2.0/24"),
        new IPRequest.IpAddressMatcher("192.168.0.0/16"),
        new IPRequest.IpAddressMatcher("198.18.0.0/15"),
        new IPRequest.IpAddressMatcher("198.51.100.0/24"),
        new IPRequest.IpAddressMatcher("203.0.113.0/24"),
        new IPRequest.IpAddressMatcher("224.0.0.0/4"),
        new IPRequest.IpAddressMatcher("240.0.0.0/4"),
        new IPRequest.IpAddressMatcher("255.255.255.255/32"),
        // IPv6
        new IPRequest.IpAddressMatcher("::/128"),
        new IPRequest.IpAddressMatcher("::1/128"),
        new IPRequest.IpAddressMatcher("::ffff:0:0/96"),
        new IPRequest.IpAddressMatcher("::/96"),
        new IPRequest.IpAddressMatcher("100::/64"),
        new IPRequest.IpAddressMatcher("2001:10::/28"),
        new IPRequest.IpAddressMatcher("2001:db8::/32"),
        new IPRequest.IpAddressMatcher("fc00::/7"),
        new IPRequest.IpAddressMatcher("fe80::/10"),
        new IPRequest.IpAddressMatcher("fec0::/10"),
        new IPRequest.IpAddressMatcher("ff00::/8"),
        // 6to4
        new IPRequest.IpAddressMatcher("2002::/24"),
        new IPRequest.IpAddressMatcher("2002:a00::/24"),
        new IPRequest.IpAddressMatcher("2002:7f00::/24"),
        new IPRequest.IpAddressMatcher("2002:a9fe::/32"),
        new IPRequest.IpAddressMatcher("2002:ac10::/28"),
        new IPRequest.IpAddressMatcher("2002:c000::/40"),
        new IPRequest.IpAddressMatcher("2002:c000:200::/40"),
        new IPRequest.IpAddressMatcher("2002:c0a8::/32"),
        new IPRequest.IpAddressMatcher("2002:c612::/31"),
        new IPRequest.IpAddressMatcher("2002:c633:6400::/40"),
        new IPRequest.IpAddressMatcher("2002:cb00:7100::/40"),
        new IPRequest.IpAddressMatcher("2002:e000::/20"),
        new IPRequest.IpAddressMatcher("2002:f000::/20"),
        new IPRequest.IpAddressMatcher("2002:ffff:ffff::/48"),
        // Teredo
        new IPRequest.IpAddressMatcher("2001::/40"),
        new IPRequest.IpAddressMatcher("2001:0:a00::/40"),
        new IPRequest.IpAddressMatcher("2001:0:7f00::/40"),
        new IPRequest.IpAddressMatcher("2001:0:a9fe::/48"),
        new IPRequest.IpAddressMatcher("2001:0:ac10::/44"),
        new IPRequest.IpAddressMatcher("2001:0:c000::/56"),
        new IPRequest.IpAddressMatcher("2001:0:c000:200::/56"),
        new IPRequest.IpAddressMatcher("2001:0:c0a8::/48"),
        new IPRequest.IpAddressMatcher("2001:0:c612::/47"),
        new IPRequest.IpAddressMatcher("2001:0:c633:6400::/56"),
        new IPRequest.IpAddressMatcher("2001:0:cb00:7100::/56"),
        new IPRequest.IpAddressMatcher("2001:0:e000::/36"),
        new IPRequest.IpAddressMatcher("2001:0:f000::/36"),
        new IPRequest.IpAddressMatcher("2001:0:ffff:ffff::/64")
    };

    private static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    public static String getClientIpAddressIfServletRequestExist() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && !ipList.isEmpty() && !"unknown".equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }
        return request.getRemoteAddr();
    }

    public static boolean isBogonRequest(String ip) {
        return Arrays.stream(IpAddressMatcherList)
            .anyMatch(ipAddressMatcher -> ipAddressMatcher.matches(ip));
    }
}
