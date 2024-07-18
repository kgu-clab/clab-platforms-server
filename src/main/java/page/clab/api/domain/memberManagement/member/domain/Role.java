package page.clab.api.domain.memberManagement.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER("ROLE_USER", "Normal User"),
    ADMIN("ROLE_ADMIN", "Administrator"),
    SUPER("ROLE_SUPER", "Super Administrator");

    private String key;
    private String description;

    public Long toRoleLevel() {
        return switch (this) {
            case USER -> 1L;
            case ADMIN -> 2L;
            case SUPER -> 3L;
            default -> 0L;
        };
    }
}
