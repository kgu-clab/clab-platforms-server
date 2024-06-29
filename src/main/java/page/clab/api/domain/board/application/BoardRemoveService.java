package page.clab.api.domain.board.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface BoardRemoveService {
    String remove(Long boardId) throws PermissionDeniedException;
}
