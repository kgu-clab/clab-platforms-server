package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.SharedAccountUsage;
import page.clab.api.type.etc.SharedAccountUsageStatus;
import page.clab.api.util.ModelMapperUtil;

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

    public static SharedAccountUsageResponseDto of(SharedAccountUsage sharedAccountUsage) {
        SharedAccountUsageResponseDto sharedAccountUsageResponseDto = ModelMapperUtil.getModelMapper().map(sharedAccountUsage, SharedAccountUsageResponseDto.class);
        sharedAccountUsageResponseDto.setUsername(sharedAccountUsage.getSharedAccount().getUsername());
        sharedAccountUsageResponseDto.setPlatformName(sharedAccountUsage.getSharedAccount().getPlatformName());
        return sharedAccountUsageResponseDto;
    }

}
