package page.clab.api.domain.community.board.application.port.in;

import page.clab.api.domain.community.board.application.dto.request.BoardRequestDto;

public interface RegisterBoardUseCase {

    String registerBoard(BoardRequestDto requestDto);
}
