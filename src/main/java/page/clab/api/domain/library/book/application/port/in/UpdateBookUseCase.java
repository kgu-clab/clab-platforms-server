package page.clab.api.domain.library.book.application.port.in;

import page.clab.api.domain.library.book.application.dto.request.BookUpdateRequestDto;

public interface UpdateBookUseCase {
    Long updateBookInfo(Long bookId, BookUpdateRequestDto requestDto);
}
