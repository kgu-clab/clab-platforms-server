package page.clab.api.external.community.board.application.port;

import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.board.domain.Board;

public interface ExternalRetrieveBoardUseCase {

    Board findByIdOrThrow(Long targetId);

    BoardCommentInfoDto getBoardCommentInfoById(Long boardId);
}
