package page.clab.api.global.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import page.clab.api.global.util.IpAddressUtil;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class IpWhitelistValidator {

    @Value("${security.whitelist.enabled}")
    private boolean whitelistEnabled;

    private final WhitelistFileLoader whitelistFileLoader;

    /**
     * 요청된 IP가 화이트리스트에 포함되는지 확인합니다.
     * @param ipAddress 확인하려는 IP 주소
     * @return IP가 화이트리스트에 포함되면 true, 그렇지 않으면 false
     */
    public boolean isIpWhitelisted(String ipAddress) {
        if (!whitelistEnabled) {
            return true;
        }

        List<String> whitelistIps = whitelistFileLoader.loadWhitelistIps();

        return whitelistIps.stream()
                .filter(Objects::nonNull)
                .anyMatch(ip -> "*".equals(ip) || IpAddressUtil.isIpInRange(ipAddress, ip));
    }
}
