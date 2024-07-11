package page.clab.api.domain.accuse.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TargetType {

    BOARD("BOARD", "게시글"),
    COMMENT("COMMENT", "댓글");

    private String key;
    private String description;
}
