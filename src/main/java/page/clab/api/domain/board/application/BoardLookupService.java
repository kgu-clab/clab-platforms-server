package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.board.application.port.in.BoardLookupUseCase;
import page.clab.api.domain.board.application.port.out.LoadBoardPort;
import page.clab.api.domain.board.domain.Board;

@Service
@RequiredArgsConstructor
public class BoardLookupService implements BoardLookupUseCase {

    private final LoadBoardPort loadBoardPort;

    @Override
    public Board getBoardByIdOrThrow(Long boardId) {
        return loadBoardPort.findByIdOrThrow(boardId);
    }
}
