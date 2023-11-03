package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {

    ACTIVE("ACTIVE", "활성 계정"),
    INACTIVE("INACTIVE", "비활성 계정");

    private String key;
    private String description;

}
