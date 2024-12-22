package page.clab.api.domain.auth.blacklistIp.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.blacklistIp.application.dto.request.BlacklistIpRequestDto;
import page.clab.api.domain.auth.blacklistIp.application.dto.response.BlacklistIpResponseDto;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;

@Component
public class BlacklistIpDtoMapper {

    public BlacklistIp fromDto(BlacklistIpRequestDto requestDto) {
        return BlacklistIp.builder()
            .ipAddress(requestDto.getIpAddress())
            .reason(requestDto.getReason())
            .build();
    }

    public BlacklistIpResponseDto toDto(BlacklistIp blacklistIp) {
        return BlacklistIpResponseDto.builder()
            .id(blacklistIp.getId())
            .ipAddress(blacklistIp.getIpAddress())
            .reason(blacklistIp.getReason())
            .createdAt(blacklistIp.getCreatedAt())
            .build();
    }
}
