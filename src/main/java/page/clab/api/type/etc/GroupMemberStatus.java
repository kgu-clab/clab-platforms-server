package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupMemberStatus {

    거절("REJECTED", "거절"),
    수락("ACCEPTED", "수락"),
    진행중("IN_PROGRESS", "진행중");

    private String key;
    private String description;
}
