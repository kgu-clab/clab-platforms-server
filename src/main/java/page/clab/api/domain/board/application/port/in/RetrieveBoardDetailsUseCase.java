package page.clab.api.domain.board.application.port.in;

import page.clab.api.domain.board.application.dto.response.BoardDetailsResponseDto;

public interface RetrieveBoardDetailsUseCase {
    BoardDetailsResponseDto retrieveBoardDetails(Long boardId);
}
