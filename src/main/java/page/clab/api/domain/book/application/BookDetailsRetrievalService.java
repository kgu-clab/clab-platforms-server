package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;

public interface BookDetailsRetrievalService {
    BookDetailsResponseDto retrieve(Long bookId);
}
