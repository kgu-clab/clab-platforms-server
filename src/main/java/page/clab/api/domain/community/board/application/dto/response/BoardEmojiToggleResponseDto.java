package page.clab.api.domain.community.board.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardEmojiToggleResponseDto {

    private Long boardId;
    private String emoji;
    private Boolean isDeleted;
}
