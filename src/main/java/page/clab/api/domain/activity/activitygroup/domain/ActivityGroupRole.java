package page.clab.api.domain.activity.activitygroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupRole {

    LEADER("LEADER", "Leader"),
    MEMBER("MEMBER", "Member"),
    NONE("NONE", "None");

    private final String key;
    private final String description;

    public boolean isNone() {
        return this.equals(NONE);
    }
}
