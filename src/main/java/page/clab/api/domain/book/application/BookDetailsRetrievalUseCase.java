package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;

public interface BookDetailsRetrievalUseCase {
    BookDetailsResponseDto retrieve(Long bookId);
}
