package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupRole {

    LEADER("LEADER", "Leader"),
    MEMBER("MEMBER", "Member");

    private String key;
    private String description;
}
