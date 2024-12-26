package page.clab.api.domain.auth.blacklistIp.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlacklistIpResponseDto {

    private Long id;
    private String ipAddress;
    private String reason;
    private LocalDateTime createdAt;
}
