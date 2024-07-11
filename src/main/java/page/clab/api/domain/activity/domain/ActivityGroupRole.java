package page.clab.api.domain.activity.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupRole {

    LEADER("LEADER", "Leader"),
    MEMBER("MEMBER", "Member"),
    NONE("NONE", "None");

    private String key;
    private String description;

}
