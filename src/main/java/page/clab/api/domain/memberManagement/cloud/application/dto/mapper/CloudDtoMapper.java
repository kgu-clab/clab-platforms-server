package page.clab.api.domain.memberManagement.cloud.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;

@Component
public class CloudDtoMapper {

    public CloudUsageInfo of(String memberId, Long usage) {
        return CloudUsageInfo.builder()
            .memberId(memberId)
            .usage(usage)
            .build();
    }
}
