package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;

public interface BookUpdateService {
    Long update(Long bookId, BookUpdateRequestDto requestDto);
}