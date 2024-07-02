package page.clab.api.domain.board.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface BoardRemoveUseCase {
    String remove(Long boardId) throws PermissionDeniedException;
}
