package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationType {

    NORMAL("NORMAL", "일반 회원"),
    OFFICER("OFFICER", "운영진"),
    CORE_TEAM("CORE_TEAM", "코어팀");

    private String key;
    private String description;

}
