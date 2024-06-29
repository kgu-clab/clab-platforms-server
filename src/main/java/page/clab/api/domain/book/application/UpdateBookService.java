package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;

public interface UpdateBookService {
    Long execute(Long bookId, BookUpdateRequestDto requestDto);
}