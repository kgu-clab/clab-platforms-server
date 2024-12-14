package page.clab.api.external.community.board.application.port;

import page.clab.api.domain.community.board.application.dto.request.BoardHashtagRequestDto;

public interface ExternalRegisterBoardHashtagUseCase {
    Long registerBoardHashtag(BoardHashtagRequestDto requestDto);
}
