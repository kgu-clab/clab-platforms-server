package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupStatus {

    WAITING("WAITING", "승인대기"),
    ACTIVE("ACTIVE", "활동중"),
    END("END", "활동종료");

    private String key;
    private String description;

}
