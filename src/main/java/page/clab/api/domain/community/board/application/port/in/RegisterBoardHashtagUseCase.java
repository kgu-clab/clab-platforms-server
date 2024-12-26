package page.clab.api.domain.community.board.application.port.in;

import page.clab.api.domain.community.board.application.dto.request.BoardHashtagRequestDto;

public interface RegisterBoardHashtagUseCase {

    Long registerBoardHashtag(BoardHashtagRequestDto requestDto);
}
