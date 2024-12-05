package page.clab.api.domain.community.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HotBoardSelectionStrategyType {

    DEFAULT("default", "댓글, 반응순 정렬");

    private final String key;
    private final String description;
}
