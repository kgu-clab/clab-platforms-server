package page.clab.api.domain.library.book.application.port.in;

import page.clab.api.domain.library.book.application.dto.request.BookRequestDto;

public interface RegisterBookUseCase {

    Long registerBook(BookRequestDto requestDto);
}
