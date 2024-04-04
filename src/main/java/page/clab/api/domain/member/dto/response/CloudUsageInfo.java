package page.clab.api.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
