package page.clab.api.domain.sharedAccount.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SharedAccountUsageStatus {

    RESERVED("RESERVED", "예약됨"),
    IN_USE("IN_USE", "이용 중"),
    COMPLETED("COMPLETED", "이용 완료"),
    CANCELED("CANCELED", "이용 취소");

    private String key;
    private String description;

}
