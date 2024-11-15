package page.clab.api.domain.community.board.application.service;

import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;

import java.util.List;

public interface HotBoardsStrategy {
    List<BoardListResponseDto> retrieveHotBoards(int size);
}
