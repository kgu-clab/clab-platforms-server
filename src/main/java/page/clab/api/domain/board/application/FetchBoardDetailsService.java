package page.clab.api.domain.board.application;

import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;

public interface FetchBoardDetailsService {
    BoardDetailsResponseDto fetchBoardDetails(Long boardId);
}
