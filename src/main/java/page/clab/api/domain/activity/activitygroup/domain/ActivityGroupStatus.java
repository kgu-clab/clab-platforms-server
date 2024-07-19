package page.clab.api.domain.activity.activitygroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupStatus {

    WAITING("WAITING", "승인 대기"),
    PROGRESSING("ACTIVE", "활동 중"),
    END("END", "활동 종료");

    private final String key;
    private final String description;
}
