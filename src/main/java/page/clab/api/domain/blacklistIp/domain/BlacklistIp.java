package page.clab.api.domain.blacklistIp.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlacklistIp {

    private Long id;
    private String ipAddress;
    private String reason;
    private LocalDateTime createdAt;

    public static BlacklistIp create(String ipAddress, String reason) {
        return BlacklistIp.builder()
                .ipAddress(ipAddress)
                .reason(reason)
                .build();
    }
}
