package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupStatus {

    승인대기("WAITING", "승인대기"),
    활동중("ACTIVE", "활동중"),
    활동종료("END", "활동종료");

    private String key;
    private String description;
}
