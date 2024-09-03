package page.clab.api.global.util;

import org.springframework.security.web.util.matcher.IpAddressMatcher;

public class IpAddressUtil {

    /**
     * 주어진 IP가 특정 서브넷이나 IP 주소와 일치하는지 확인합니다.
     * @param ipAddress 확인하려는 대상 IP 주소
     * @param ipOrCidr 서브넷 마스크를 포함한 IP 주소 (예: 192.168.1.0/24 또는 단일 IP 주소)
     * @return 주어진 IP가 서브넷 내에 있거나 IP 주소와 일치하면 true, 그렇지 않으면 false
     */
    public static boolean isIpInRange(String ipAddress, String ipOrCidr) {
        try {
            IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipOrCidr);
            return ipAddressMatcher.matches(ipAddress);
        } catch (Exception e) {
            return false;
        }
    }
}
