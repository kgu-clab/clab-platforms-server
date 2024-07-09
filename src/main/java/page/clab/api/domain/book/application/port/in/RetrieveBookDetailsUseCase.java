package page.clab.api.domain.book.application.port.in;

import page.clab.api.domain.book.application.dto.response.BookDetailsResponseDto;

public interface RetrieveBookDetailsUseCase {
    BookDetailsResponseDto retrieveBookDetails(Long bookId);
}
