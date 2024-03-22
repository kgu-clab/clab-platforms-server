package page.clab.api.domain.sharedAccount.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsage;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedAccountUsageResponseDto {

    private Long id;

    private String username;

    private String platformName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String memberId;

    private SharedAccountUsageStatus status;

    private LocalDateTime createdAt;

    public static SharedAccountUsageResponseDto toDto(SharedAccountUsage sharedAccountUsage) {
        return SharedAccountUsageResponseDto.builder()
                .id(sharedAccountUsage.getId())
                .username(sharedAccountUsage.getSharedAccount().getUsername())
                .platformName(sharedAccountUsage.getSharedAccount().getPlatformName())
                .startTime(sharedAccountUsage.getStartTime())
                .endTime(sharedAccountUsage.getEndTime())
                .memberId(sharedAccountUsage.getMemberId())
                .status(sharedAccountUsage.getStatus())
                .createdAt(sharedAccountUsage.getCreatedAt())
                .build();
    }

}
