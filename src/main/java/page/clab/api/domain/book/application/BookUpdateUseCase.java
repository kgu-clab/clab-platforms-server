package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;

public interface BookUpdateUseCase {
    Long update(Long bookId, BookUpdateRequestDto requestDto);
}