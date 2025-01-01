package page.clab.api.domain.memberManagement.position.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PositionType {

    PRESIDENT("PRESIDENT", "회장", 1),
    VICE_PRESIDENT("VICE_PRESIDENT", "부회장", 2),
    OPERATION("OPERATION", "운영진", 3),
    CORE_TEAM("CORE_TEAM", "코어팀", null),
    MEMBER("MEMBER", "일반회원", null);

    @Enumerated(EnumType.STRING)
    private final String key;
    private final String description;
    private final Integer priority;

    public static Integer getPriorityByKey(String key) {
        for (PositionType positionType : values()) {
            if (positionType.getKey().equals(key)) {
                return positionType.getPriority();
            }
        }
        throw new IllegalArgumentException("Invalid PositionType key: " + key);
    }
}
