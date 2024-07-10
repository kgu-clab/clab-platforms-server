package page.clab.api.domain.board.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;

@Getter
@Builder
public class BoardCommentInfoDto {

    private final Long boardId;
    private final String memberId;
    private final String title;
    private final BoardCategory category;

    public static BoardCommentInfoDto create(Board board) {
        return BoardCommentInfoDto.builder()
                .boardId(board.getId())
                .memberId(board.getMemberId())
                .title(board.getTitle())
                .category(board.getCategory())
                .build();
    }
}
