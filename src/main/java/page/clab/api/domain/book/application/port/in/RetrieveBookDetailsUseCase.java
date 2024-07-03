package page.clab.api.domain.book.application.port.in;

import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;

public interface RetrieveBookDetailsUseCase {
    BookDetailsResponseDto retrieve(Long bookId);
}
