package page.clab.api.domain.hiring.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationType {

    NORMAL("NORMAL", "일반 회원"),
    OPERATION("OPERATION", "운영진"),
    CORE_TEAM("CORE_TEAM", "코어팀");

    private final String key;
    private final String description;
}
