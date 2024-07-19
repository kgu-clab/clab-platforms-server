package page.clab.api.domain.community.board.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveBoardUseCase {
    String removeBoard(Long boardId) throws PermissionDeniedException;
}
