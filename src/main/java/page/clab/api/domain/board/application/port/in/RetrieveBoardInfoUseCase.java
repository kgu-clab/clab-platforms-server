package page.clab.api.domain.board.application.port.in;

import page.clab.api.domain.board.application.dto.shared.BoardCommentInfoDto;

public interface RetrieveBoardInfoUseCase {
    BoardCommentInfoDto getBoardCommentInfoById(Long boardId);
}
