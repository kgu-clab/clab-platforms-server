package page.clab.api.domain.member.domain;

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

    public Long toLong() {
        switch (this) {
            case USER:
                return 1L;
            case ADMIN:
                return 2L;
            case SUPER:
                return 3L;
            default:
                return 0L;
        }
    }

}
