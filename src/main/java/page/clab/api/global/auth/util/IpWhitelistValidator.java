package page.clab.api.global.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import page.clab.api.global.util.IpAddressUtil;

import java.util.List;
import java.util.Objects;

/**
 * IpWhitelistValidator는 요청된 IP 주소가 화이트리스트에 포함되는지 확인하는 유틸리티 클래스입니다.
 *
 * <p>화이트리스트 검증 기능은 애플리케이션 설정에 따라 활성화되며, 외부에서 설정된 IP 목록을 통해
 * IP 접근을 제한할 수 있습니다.</p>
 *
 * <p>주요 기능:</p>
 * <ul>
 *     <li>{@link #isIpWhitelisted(String)}: 특정 IP 주소가 화이트리스트에 포함되는지 확인</li>
 * </ul>
 *
 * <p>화이트리스트 기능이 활성화되지 않은 경우 기본적으로 모든 IP 접근이 허용됩니다.</p>
 *
 * @see WhitelistFileLoader
 */
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

        if (whitelistIps.isEmpty()) {
            return false;
        }

        return whitelistIps.stream()
                .filter(Objects::nonNull)
                .anyMatch(ip -> "*".equals(ip) || IpAddressUtil.isIpInRange(ipAddress, ip));
    }
}
