package page.clab.api.domain.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.board.application.port.in.RetrieveBoardInfoUseCase;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;

@Service
@RequiredArgsConstructor
public class BoardInfoRetrievalService implements RetrieveBoardInfoUseCase {

    private final RetrieveBoardPort retrieveBoardPort;

    @Override
    public BoardCommentInfoDto getBoardCommentInfoById(Long boardId) {
        Board board = retrieveBoardPort.findByIdOrThrow(boardId);
        return BoardCommentInfoDto.create(board);
    }
}
