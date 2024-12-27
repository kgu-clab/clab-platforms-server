package page.clab.api.domain.memberManagement.executive.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExecutivePosition {

    PRESIDENT("PRESIDENT", "회장"),
    VICE_PRESIDENT("VICE_PRESIDENT", "부회장"),
    GENERAL("GENERAL", "일반 운영진");

    private final String key;
    private final String description;
}
