package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER("ROLE_USER", "Normal User"),
    ADMIN("ROLE_ADMIN", "Administrator"),
    TEAM_LEADER("ROLE_TEAM_LEADER", "Team Leader"),
    SUPER("ROLE_SUPER", "Super Administrator");

    private String key;
    private String description;

}
