package page.clab.api.domain.memberManagement.position.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PositionType {

    PRESIDENT("PRESIDENT", "회장"),
    VICE_PRESIDENT("VICE_PRESIDENT", "부회장"),
    OPERATION("OPERATION", "운영진"),
    CORE_TEAM("CORE_TEAM", "코어팀"),
    MEMBER("MEMBER", "일반회원");

    @Enumerated(EnumType.STRING)
    private String key;
    private String description;
}
