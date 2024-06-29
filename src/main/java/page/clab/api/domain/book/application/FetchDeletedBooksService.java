package page.clab.api.domain.book.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.dto.response.BookDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchDeletedBooksService {
    PagedResponseDto<BookDetailsResponseDto> execute(Pageable pageable);
}
