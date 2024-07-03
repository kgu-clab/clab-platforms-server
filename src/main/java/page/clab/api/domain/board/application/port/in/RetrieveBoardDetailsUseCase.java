package page.clab.api.domain.board.application.port.in;

import page.clab.api.domain.board.dto.response.BoardDetailsResponseDto;

public interface RetrieveBoardDetailsUseCase {
    BoardDetailsResponseDto retrieve(Long boardId);
}
