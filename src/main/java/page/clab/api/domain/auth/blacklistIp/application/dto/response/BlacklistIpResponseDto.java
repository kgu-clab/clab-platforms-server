package page.clab.api.domain.auth.blacklistIp.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlacklistIpResponseDto {

    private Long id;
    private String ipAddress;
    private String reason;
    private LocalDateTime createdAt;

    public static BlacklistIpResponseDto toDto(BlacklistIp blacklistIp) {
        return BlacklistIpResponseDto.builder()
                .id(blacklistIp.getId())
                .ipAddress(blacklistIp.getIpAddress())
                .reason(blacklistIp.getReason())
                .createdAt(blacklistIp.getCreatedAt())
                .build();
    }
}
