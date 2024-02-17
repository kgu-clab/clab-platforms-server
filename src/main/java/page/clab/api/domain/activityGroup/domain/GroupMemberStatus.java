package page.clab.api.domain.activityGroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupMemberStatus {

    REJECTED("REJECTED", "거절"),
    ACCEPTED("ACCEPTED", "수락"),
    WAITING("WAITING", "승인 대기");

    private String key;
    private String description;

}