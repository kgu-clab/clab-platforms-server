package page.clab.api.domain.book.application.port.in;

import page.clab.api.domain.book.dto.request.BookUpdateRequestDto;

public interface UpdateBookUseCase {
    Long updateBookInfo(Long bookId, BookUpdateRequestDto requestDto);
}
