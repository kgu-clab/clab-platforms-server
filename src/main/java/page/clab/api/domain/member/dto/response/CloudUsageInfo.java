package page.clab.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
