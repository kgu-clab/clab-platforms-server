package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupMemberStatus {

    REJECTED("REJECTED", "거절"),
    ACCEPTED("ACCEPTED", "수락"),
    IN_PROGRESS("IN_PROGRESS", "진행중");

    private String key;
    private String description;
}