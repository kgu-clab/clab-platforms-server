package page.clab.api.domain.memberManagement.member.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CloudUsageInfo {

    private String memberId;
    private Long usage;

    public static CloudUsageInfo create(String memberId, Long usage) {
        return CloudUsageInfo.builder()
                .memberId(memberId)
                .usage(usage)
                .build();
    }
}
