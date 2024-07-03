package page.clab.api.domain.board.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveBoardUseCase {
    String remove(Long boardId) throws PermissionDeniedException;
}
