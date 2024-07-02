package page.clab.api.domain.board.application;

import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;

public interface BoardDetailsRetrievalUseCase {
    BoardDetailsResponseDto retrieve(Long boardId);
}
