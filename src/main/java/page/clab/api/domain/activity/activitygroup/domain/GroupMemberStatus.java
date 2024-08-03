package page.clab.api.domain.activity.activitygroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupMemberStatus {

    REJECTED("REJECTED", "거절"),
    ACCEPTED("ACCEPTED", "수락"),
    WAITING("WAITING", "승인 대기");

    private final String key;
    private final String description;
}
