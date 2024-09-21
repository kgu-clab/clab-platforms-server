package page.clab.api.domain.activity.activitygroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupMemberStatus {

    WAITING("WAITING", "승인 대기"),
    ACCEPTED("ACCEPTED", "수락"),
    REJECTED("REJECTED", "거절");

    private final String key;
    private final String description;
}
