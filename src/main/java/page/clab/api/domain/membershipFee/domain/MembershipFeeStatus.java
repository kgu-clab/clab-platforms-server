package page.clab.api.domain.membershipFee.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MembershipFeeStatus {

    PENDING("PENDING", "대기"),
    HOLD("HOLD", "보류"),
    APPROVED("APPROVED", "승인"),
    REJECTED("REJECTED", "반려");

    private String key;
    private String description;
}
