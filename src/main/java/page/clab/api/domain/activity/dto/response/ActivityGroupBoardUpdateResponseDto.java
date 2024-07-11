package page.clab.api.domain.activity.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.domain.ActivityGroupBoard;

@Getter
@Builder
public class ActivityGroupBoardUpdateResponseDto {

    private Long id;

    private Long parentId;

    public static ActivityGroupBoardUpdateResponseDto toDto(ActivityGroupBoard board) {
        return ActivityGroupBoardUpdateResponseDto.builder()
                .id(board.getId())
                .parentId(board.getParent() != null ? board.getParent().getId() : null)
                .build();
    }

}
