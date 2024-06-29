package page.clab.api.domain.board.application;

import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;

public interface BoardDetailsRetrievalService {
    BoardDetailsResponseDto retrieve(Long boardId);
}
