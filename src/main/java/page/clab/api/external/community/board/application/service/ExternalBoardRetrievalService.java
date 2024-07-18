package page.clab.api.external.community.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.board.application.service.BoardRetrievalService;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.external.community.board.application.port.ExternalRetrieveBoardUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBoardRetrievalService implements ExternalRetrieveBoardUseCase {

    private final BoardRetrievalService boardRetrievalService;

    @Override
    public Board findByIdOrThrow(Long targetId) {
        return boardRetrievalService.findByIdOrThrow(targetId);
    }

    @Override
    public BoardCommentInfoDto getBoardCommentInfoById(Long boardId) {
        Board board = boardRetrievalService.findByIdOrThrow(boardId);
        return BoardCommentInfoDto.create(board);
    }
}
