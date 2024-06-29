package page.clab.api.domain.board.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteBoardService {
    String deleteBoard(Long boardId) throws PermissionDeniedException;
}
