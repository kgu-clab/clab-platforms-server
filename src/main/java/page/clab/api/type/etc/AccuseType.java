package page.clab.api.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccuseType {

    BOARD("BOARD", "게시글"),
    COMMENT("COMMENT", "댓글"),
    REVIEW("REVIEW", "후기");

    private String key;
    private String description;

}
