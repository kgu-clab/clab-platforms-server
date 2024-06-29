package page.clab.api.domain.book.application;

import page.clab.api.domain.book.dto.request.BookRequestDto;

public interface CreateBookService {
    Long execute(BookRequestDto requestDto);
}